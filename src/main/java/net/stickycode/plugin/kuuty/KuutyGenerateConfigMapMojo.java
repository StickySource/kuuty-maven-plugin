package net.stickycode.plugin.kuuty;

import java.io.File;

import javax.inject.Inject;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import net.stickycode.kuuty.model.v18.IoK8sApiCoreV1ConfigMap;

/**
 * Create a configmap.yaml from a number of template files
 */
@Mojo(threadSafe = true, name = "generate-config", requiresDirectInvocation = false, requiresProject = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class KuutyGenerateConfigMapMojo
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
   * The name of the config map resources in Kubernetes
   */
  @Parameter(defaultValue = "${project.name}", required = true)
  String name;

  /**
   * Where to write the generated config map
   */
  @Parameter(defaultValue = "${project.build.directory}/resources/kubernetes", required = true)
  private File outputDirectory;

  @Inject
  KuutyTemplateCollector collector;

  @Inject
  KuutyResourceGenerator generator;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    KuutyTemplateProcessor<IoK8sApiCoreV1ConfigMap> processor = new KuutyConfigMapProcessor(name);
    collector.processSourceDirectory(configDirectory.toPath(), processor);
    generator.write(processor.getResource(), outputDirectory.toPath(), filename);
  }

}
