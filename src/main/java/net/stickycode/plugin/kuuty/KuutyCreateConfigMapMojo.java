package net.stickycode.plugin.kuuty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.openapitools.client.model.IoK8sApiCoreV1ConfigMap;
import org.yaml.snakeyaml.Yaml;

/**
 * Create a configmap.yaml from a number of template files
 */
@Mojo(threadSafe = true, name = "create-configmap", requiresDirectInvocation = false, requiresProject = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class KuutyCreateConfigMapMojo
    extends AbstractMojo {

  /**
   * Directory containing the config template files to encapsulate
   */
  @Parameter(defaultValue = "src/main/config", required = true)
  private File configDirectory;

  /**
   * The name of the config map file to create
   */
  @Parameter(defaultValue = "configmap.yaml", required = true)
  private String filename;

  /**
   * Where to write the generated config map
   */
  @Parameter(defaultValue = "${project.build.directory}/resources/kubernetes", required = true)
  private File outputDirectory;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    verifyConfiguration();

    IoK8sApiCoreV1ConfigMap configmap = processConfigDirectory(configDirectory.toPath());
    generateFile(configmap, outputPath());
  }

  protected void verifyConfiguration() throws MojoFailureException {
    if (!configDirectory.isDirectory())
      throw new MojoFailureException("base directory not found " + configDirectory);

    if (!configDirectory.canRead())
      throw new MojoFailureException("base directory cannot be read" + configDirectory);

    try {
      Files.createDirectories(outputDirectory.toPath());
    }
    catch (IOException e) {
      throw new MojoFailureException("Could not create output directory " + outputDirectory, e);
    }
  }

  protected IoK8sApiCoreV1ConfigMap processConfigDirectory(Path configPath) {
    IoK8sApiCoreV1ConfigMap configmap = new IoK8sApiCoreV1ConfigMap();
    try {
      Files.walk(configPath)
        .filter(Files::isReadable)
        .filter(Files::isRegularFile)
        .forEach(f -> processFile(configmap, f));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    return configmap;
  }

  protected void processFile(IoK8sApiCoreV1ConfigMap configmap, Path f) {
    try {
      String name = f.getFileName().toString();
      String body = Files.readString(f);
      configmap.putDataItem(name, body);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void generateFile(IoK8sApiCoreV1ConfigMap configmap, Path outputPath) {
    Yaml yaml = new Yaml();
    try (BufferedWriter writer = Files.newBufferedWriter(outputPath);) {
      yaml.dump(configmap, writer);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected Path outputPath() {
    return outputDirectory.toPath().resolve(filename);
  }

}
