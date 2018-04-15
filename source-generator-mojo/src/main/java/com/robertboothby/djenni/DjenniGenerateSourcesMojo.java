package com.robertboothby.djenni;

import com.robertboothby.template.AbstractGeneratorMojo;
import com.robertboothby.template.model.GenerationModel;
import com.robertboothby.utilities.lambda.FunctionResult;
import com.robertboothby.template.model.GenerationModelRetriever;
import com.robertboothby.utilities.lambda.LambdaUtilities;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.robertboothby.utilities.lambda.LambdaUtilities.not;
import static com.robertboothby.utilities.lambda.LambdaUtilities.wrap;
import static java.util.Arrays.stream;


@Mojo(name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class DjenniGenerateSourcesMojo extends AbstractGeneratorMojo {

    @Parameter(required = true)
    private FileSet[] fileSets;

    private final FileSetManager fileSetManager = new FileSetManager();

    @Parameter(defaultValue = "false")
    private boolean generateForCollectionGetters;

    @Override
    protected GenerationModelRetriever getGenerationModelRetriever() throws MojoExecutionException {
        //Been thinking about how to provide default (not abstract) implementations of the Builders.
        //Should always use the builders that are generated and should also consider looking for pre-generated builders
        //either on the classpath or on the sources directory.
        //Also need to think about the default builders for primitive types.
        //Also need to provide a configuration mechanism to provide point overrides to the generation.


        JavaProjectBuilder builder = new JavaProjectBuilder();

        List<FunctionResult<JavaSource>> results =
                stream(fileSets)
                .flatMap(this::getIncludedFiles)
                .collect(Collectors.toSet()) //Ensure uniqueness
                .stream()
                .map(wrap(builder::addSource))
                .collect(Collectors.toList());

        //Check for failures
        String failures = results.stream()
                .filter(FunctionResult::isExceptional)
                .map(FunctionResult::toString)
                .collect(Collectors.joining("\n"));

        if(failures != null && !failures.isEmpty()){
            throw new MojoExecutionException("Failed to parse files: \n" + failures);
        }

        return () -> results
                .stream()
                .map(FunctionResult::getResult)
                .map(Optional::get)
                .flatMap(this::createGenerationModel)
                .collect(Collectors.toList());
    }

    private Stream<GenerationModel> createGenerationModel(JavaSource javaSource) {
        //Filter once so that we can reuse it.
        List<JavaClass> filteredJavaClasses = javaSource.getClasses()
                .stream()
                .filter(not(JavaClass::isInterface))
                .filter(not(JavaClass::isInner))
                .filter(not(JavaClass::isAbstract))
                .filter(not(JavaClass::isEnum))
                .filter(JavaClass::isPublic)
                .collect(Collectors.toList());

        return Stream.concat(getGeneratorModels(javaSource, filteredJavaClasses), getGeneratorBuilderModels(javaSource, filteredJavaClasses));
    }

    private Stream<GenerationModel> getGeneratorBuilderModels(JavaSource javaSource, List<JavaClass> filteredJavaClasses) {
        return filteredJavaClasses
                .stream()
                .map(javaClass ->
                        new GenerationModel(
                                "Generator.ftl",
                                getModel(javaSource, javaClass),
                                javaClass.getFullyQualifiedName().replace('.', '/') + "Generator.java"
                        )
                );
    }

    private Stream<GenerationModel> getGeneratorModels(JavaSource javaSource, List<JavaClass> filteredJavaClasses) {
        return filteredJavaClasses
                .stream()
                .map(javaClass ->
                        new GenerationModel(
                                "GeneratorBuilder.ftl",
                                getModel(javaSource, javaClass),
                                javaClass.getFullyQualifiedName().replace('.', '/') + "GeneratorBuilder.java"
                        )
                );
    }

    private Map<String, Object> getModel(JavaSource javaSource, JavaClass javaClass) {
        Map<String, Object> result = new HashMap<>();
        result.put("javaSource", javaSource);
        result.put("javaClass", javaClass);
        return result;
    }

    private Stream<File> getIncludedFiles(FileSet fileSet) {
        return stream(fileSetManager.getIncludedFiles(fileSet))
                .map(file -> new File(new File(fileSet.getDirectory()), file));
    }
}
