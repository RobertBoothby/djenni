package com.robertboothby.djenni;

import com.robertboothby.djenni.sourcegenerator.SourceCodeGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.utils.io.DirectoryScanner;
import org.djenni.sourcegenerator.SourceCodeGenerator;

import java.io.File;
import java.io.IOException;

@Mojo(name = "djenni-source-maven-plugin", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class DjenniGenerateSourcesMojo extends AbstractMojo {

    @Parameter(required = true)
    private File[] sourceRoots;

    @Parameter
    private String[] includes;

    @Parameter
    private String[] excludes;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/djenni")
    private File sourceDestination;

    @Parameter(defaultValue = "${project.build.directory}/generated-test-sources/djenni")
    private File testSourceDestination;

    @Parameter(defaultValue = "${project}", required = true)
    private MavenProject project;

    @Parameter(defaultValue = "false")
    private boolean isGeneratingTestSources;

    @Parameter(defaultValue = "false")
    private boolean generateForCollectionGetters;

    public void execute() throws MojoExecutionException {

        SourceCodeGenerator sourceCodeGenerator = new SourceCodeGenerator();
        final File destinationRoot = isGeneratingTestSources ? testSourceDestination : sourceDestination;
        sourceCodeGenerator.setDestinationRoot(destinationRoot);

        getLog().info("Generating Djenni Sources");

        try{
            getLog().info("Destination : " + destinationRoot.getCanonicalPath());
        } catch (IOException e) {
            throw new MojoExecutionException("Could not resolve destination directory", e);
        }

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        for (File sourceRoot : sourceRoots){
            scanner.setBasedir(sourceRoot);
            scanner.scan();
            String[] files = scanner.getIncludedFiles();
            try {
                sourceCodeGenerator.generateSourceFiles(true, generateForCollectionGetters, sourceRoot, files);
            } catch (IOException e) {
                throw new MojoExecutionException("Problem with processing a source root : " + sourceRoot, e);
            }
        }

        if(isGeneratingTestSources){
            project.addTestCompileSourceRoot(destinationRoot.getAbsolutePath());
        } else {
            project.addCompileSourceRoot(destinationRoot.getAbsolutePath());
        }

    }
}
