package com.robertboothby.djenni;

import com.robertboothby.template.AbstractGeneratorMojo;
import com.robertboothby.utilities.lambda.FunctionResult;
import com.robertboothby.template.model.GenerationModelRetriever;
import com.robertboothby.utilities.lambda.LambdaUtilities;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<FunctionResult<JavaSource>> sourceFiles =
                stream(fileSets)
                .flatMap(this::getIncludedFiles)
                .collect(Collectors.toSet()) //Ensure uniqueness
                .stream()
                .map(wrap(builder::addSource))
                .collect(Collectors.toList());

        //Check for failures
        String failures = sourceFiles.stream()
                .filter(FunctionResult::isExceptional)
                .map(FunctionResult::toString)
                .collect(Collectors.joining("\n"));

        if(failures != null && !failures.isEmpty()){
            throw new MojoExecutionException("Failed to parse files: \n" + failures);
        }


        return null;
    }

    private Stream<File> getIncludedFiles(FileSet fileSet) {
        return stream(fileSetManager.getIncludedFiles(fileSet))
                .map(file -> new File(new File(fileSet.getDirectory()), file));
    }


//    public void execute() throws MojoExecutionException {
//
//        SourceCodeGenerator sourceCodeGenerator = new SourceCodeGenerator();
//        final File destinationRoot = isGeneratingTestSources ? testSourceDestination : sourceDestination;
//        sourceCodeGenerator.setDestinationRoot(destinationRoot);
//
//        getLog().info("Generating Djenni Sources");
//
//        try{
//            getLog().info("Destination : " + destinationRoot.getCanonicalPath());
//        } catch (IOException e) {
//            throw new MojoExecutionException("Could not resolve destination directory", e);
//        }
//
//        DirectoryScanner scanner = new DirectoryScanner();
//        scanner.setIncludes(includes);
//        scanner.setExcludes(excludes);
//        for (File sourceRoot : sourceRoots){
//            scanner.setBasedir(sourceRoot);
//            scanner.scan();
//            String[] files = scanner.getIncludedFiles();
//            try {
//                sourceCodeGenerator.generateSourceFiles(true, generateForCollectionGetters, sourceRoot, files);
//            } catch (IOException e) {
//                throw new MojoExecutionException("Problem with processing a source root : " + sourceRoot, e);
//            }
//        }
//
//        if(isGeneratingTestSources){
//            project.addTestCompileSourceRoot(destinationRoot.getAbsolutePath());
//        } else {
//            project.addCompileSourceRoot(destinationRoot.getAbsolutePath());
//        }
//
//    }
}
