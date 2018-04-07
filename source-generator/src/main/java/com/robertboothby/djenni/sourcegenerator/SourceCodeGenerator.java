package com.robertboothby.djenni.sourcegenerator;

import com.robertboothby.djenni.util.Nullable;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaConstructor;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.djenni.util.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.djenni.util.Nullable.nullValue;
import static org.djenni.util.Nullable.nullable;

/**
 *
 * <p>If the class has only one constructor then there is no need for an annotation otherwise the constructor to be
 * generated must be annotated with {@link ConstructorForGeneration}</p>
 * <p>&#169; 2014 Forest View Developments Ltd.</p>
 *
 * @author robertboothby
 */
public class SourceCodeGenerator {

    public static final String DEFAULT_SOURCE_DESTINATION_ROOT = "./target/generated-sources/datagen";

    private static final GeneratedFileType GENERATOR = new GeneratedFileType("{0}Generator.java",
            "/com/djenni/templates/velocity/generator.ft");
    private static final GeneratedFileType GENERATOR_BUILDER = new GeneratedFileType("Abstract{0}GeneratorBuilder.java",
            "/com/djenni/templates/velocity/generator_builder.ft");

    private final VelocityEngine velocityEngine;
    private File destinationRoot = new File(DEFAULT_SOURCE_DESTINATION_ROOT);

    public SourceCodeGenerator() {
        Properties engineProperties = new Properties();
        engineProperties.setProperty(Velocity.RESOURCE_LOADER, "class");
        engineProperties.setProperty("class." + Velocity.RESOURCE_LOADER + ".class",
                ClasspathResourceLoader.class.getName());

        velocityEngine = new VelocityEngine();
        velocityEngine.init(engineProperties);
    }

    public void setDestinationRoot(File destinationRoot) {
        this.destinationRoot = destinationRoot;
    }

    public void generateSourceFiles(boolean overwrite, boolean generateForCollectionGetters,
                                    File sourceRoot, String ... sourceFiles) throws IOException {
        JavaProjectBuilder projectBuilder = new JavaProjectBuilder();
        for(String sourceFileString : sourceFiles){
            File sourceFile = new File(sourceRoot, sourceFileString);
            if(sourceFile.isDirectory()){
                projectBuilder.addSourceTree(sourceFile);
            } else {
                projectBuilder.addSource(sourceFile);
            }
        }

        for(JavaClass javaClass : projectBuilder.getClasses()) {
            if(isSuitableForGeneration(javaClass)) {
                generateSourceFilesFor(javaClass, overwrite, generateForCollectionGetters);
            }
        }

    }

    private boolean isSuitableForGeneration(JavaClass javaClass) {
        return !javaClass.isEnum()
                &&  !javaClass.isInner()
                &&  !javaClass.isInterface()
                &&  !javaClass.isAbstract()
                &&  javaClass.isPublic();
    }

    private void generateSourceFilesFor(
            JavaClass javaClass, boolean overwrite, boolean generateForCollectionGetters) throws IOException {

        Nullable<JavaConstructor> constructorForGenerator = getConstructorForGenerator(javaClass);

        if(constructorForGenerator.hasValue()) {
            VelocityContext velocityContext = initialiseVelocityContext(
                    javaClass, generateForCollectionGetters, constructorForGenerator.value());

            File generatedSourceDestinationDirectory = new File(
                    destinationRoot, javaClass.getPackageName().replace('.', '/'));
            generatedSourceDestinationDirectory.mkdirs();

            generateSourceFileForClass(
                    javaClass, overwrite, velocityContext, generatedSourceDestinationDirectory, GENERATOR);

            generateSourceFileForClass(
                    javaClass, overwrite, velocityContext, generatedSourceDestinationDirectory, GENERATOR_BUILDER);
        } else {
            //TODO work out how to report on absence of constructor for the class.
        }
    }

    private VelocityContext initialiseVelocityContext(JavaClass javaClass, boolean generateForCollectionGetters, JavaConstructor constructor) {
        List<JavaMethod> setters = getSetters(javaClass);
        List<JavaMethod> collectionGetters = generateForCollectionGetters ? getCollectionGetters(javaClass) : new ArrayList<JavaMethod>();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("class", javaClass);
        velocityContext.put("constructor", constructor);
        velocityContext.put("setterMethods", setters);
        velocityContext.put("collectionGetters", collectionGetters);
        return velocityContext;
    }

    private Nullable<JavaConstructor> getConstructorForGenerator(JavaClass javaClass) {
        List<JavaConstructor> candidateConstructors = javaClass.getConstructors();
        if(candidateConstructors.size() == 1){
            return Nullable.nullable(candidateConstructors.get(0));
        }

        for(JavaConstructor candidateConstructor : candidateConstructors){
            if(candidateConstructor.getTagByName(ConstructorForGeneration.class.getSimpleName()) != null){
                return Nullable.nullable(candidateConstructor);
            }
        }
        return Nullable.nullValue();
    }

    private List<JavaMethod> getSetters(JavaClass javaClass) {
        List<JavaMethod> setterMethods = new ArrayList<>();
        for(BeanProperty candidateProperty : javaClass.getBeanProperties(true)){
            final JavaMethod candidatePropertyMutator = candidateProperty.getMutator();
            if(candidatePropertyMutator != null){
                setterMethods.add(candidatePropertyMutator);
            }
        }
        return setterMethods;
    }

    private List<JavaMethod> getCollectionGetters(JavaClass javaClass) {
        List<JavaMethod> collectionGetters = new ArrayList<>();
        for(BeanProperty candidateProperty : javaClass.getBeanProperties(true)){
            JavaMethod candidatePropertyAccessor = candidateProperty.getAccessor();
            if(     candidatePropertyAccessor != null
                    &&
                    candidatePropertyAccessor.getReturns().isA(Collection.class.getName())){
                collectionGetters.add(candidatePropertyAccessor);
            }
        }
        return collectionGetters;
    }

    private void generateSourceFileForClass(JavaClass javaClass, boolean overwrite, VelocityContext velocityContext, File generatedSourceDestinationDirectory, GeneratedFileType fileType) throws IOException {
        File generatorSourceDestinationFile = new File(generatedSourceDestinationDirectory, fileType.pattern.format(new Object[]{javaClass.getName()}));
        if(overwrite && generatorSourceDestinationFile.exists()){
            generatorSourceDestinationFile.delete();
        }

        if(!generatorSourceDestinationFile.exists()){
            try(FileWriter sourceCodeWriter = new FileWriter(generatorSourceDestinationFile)){
                velocityEngine.mergeTemplate(fileType.resourceName, StandardCharsets.UTF_8.name(), velocityContext, sourceCodeWriter);
            }
        } else {
            //TODO work out how best to report on unexpected presence of supposedly absent file.
        }
    }

    private static final class GeneratedFileType {
        private final MessageFormat pattern;
        private final String resourceName;

        private GeneratedFileType(String pattern, String resourceName) {
            this.pattern = new MessageFormat(pattern);
            this.resourceName = resourceName;
        }
    }
}
