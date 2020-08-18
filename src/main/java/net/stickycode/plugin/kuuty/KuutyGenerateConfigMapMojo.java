package net.stickycode.plugin.kuuty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import net.stickycode.kuuty.model.v18.IoK8sApiCoreV1ConfigMap;
import net.stickycode.kuuty.model.v18.IoK8sApimachineryPkgApisMetaV1ObjectMeta;

/**
 * Create a configmap.yaml from a number of template files
 */
@Mojo(threadSafe = true, name = "generate-config", requiresDirectInvocation = false, requiresProject = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class KuutyGenerateConfigMapMojo
    extends AbstractMojo
    implements KuutyTemplateCollectorConfiguration {

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

  @Inject
  KuutyTemplateCollector collector;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    collector.verifyConfiguration(this);

    IoK8sApiCoreV1ConfigMap configmap = processConfigDirectory(configDirectory.toPath());
    generateFile(configmap, outputPath());
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
    DumperOptions options = new DumperOptions();
    options.setPrettyFlow(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setCanonical(false);
    options.setExplicitStart(false);
    Yaml yaml = new Yaml(new KuutyRepresenter(), options);
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

  public IoK8sApiCoreV1ConfigMap createConfigMap() {
    IoK8sApiCoreV1ConfigMap config = new IoK8sApiCoreV1ConfigMap();
    config.setApiVersion("apps/v1");
    config.setKind("ConfigMap");
    IoK8sApimachineryPkgApisMetaV1ObjectMeta metadata = new IoK8sApimachineryPkgApisMetaV1ObjectMeta();
    metadata.setName("config");
    config.setMetadata(metadata);
    return config;
  }


  @Override
  public Path getSourceDirectory() {
    return configDirectory.toPath();
  }


  @Override
  public Path getOutputDirectory() {
    return outputDirectory.toPath();
  }

}
