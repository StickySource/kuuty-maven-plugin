package net.stickycode.plugin.kuuty;

import java.io.File;

import javax.inject.Inject;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(threadSafe = true, name = "generate-secret", requiresDirectInvocation = false, requiresProject = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class KuutyGenerateSecretMojo
    extends AbstractMojo {

  /**
   * Directory containing the config template files to encapsulate
   */
  @Parameter(defaultValue = "src/main/secret", required = true)
  private File sourceDirectory;

  /**
   * The name of the secret file to create
   */
  @Parameter(defaultValue = "secret.yaml", required = true)
  private String filename;

  /**
   * The name of the secret resource in Kubernetes
   */
  @Parameter(defaultValue = "${project.name}", required = true)
  String name;

  /**
   * Where to write the generated secret
   */
  @Parameter(defaultValue = "${project.build.directory}/resources/kubernetes", required = true)
  private File outputDirectory;

  /**
   * The path in the outputDirectory to place the files, useful when you are overriding/embellishing a Software Product or other
   * Kubernetes resources aggregation
   */
  @Parameter(defaultValue = "")
  private String outputContextPath = "";

  @Inject
  KuutyTemplateCollector collector;

  @Inject
  KuutyResourceGenerator generator;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    KuutySecretProcessor processor = new KuutySecretProcessor(name);
    collector.processSourceDirectory(sourceDirectory.toPath(), processor);
    generator.write(processor.getResource(), outputDirectory.toPath().resolve(outputContextPath), filename);
  }

}
