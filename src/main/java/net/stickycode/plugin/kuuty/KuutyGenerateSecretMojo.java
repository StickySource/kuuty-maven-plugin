package net.stickycode.plugin.kuuty;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(threadSafe = true, name = "generate-secret", requiresDirectInvocation = false, requiresProject = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class KuutyGenerateSecretMojo
    extends KuutyMojo {

  /**
   * Directory containing the secret sample files to encapsulate
   */
  @Parameter(defaultValue = "src/main/secrets", required = true)
  private File sourceDirectory;

  /**
   * The name of the secret file to create
   */
  @Parameter(defaultValue = "secret.yaml", required = true)
  private String filename;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    KuutySecretProcessor processor = new KuutySecretProcessor(name, namespace, type);
    collector.processSourceDirectory(sourceDirectory.toPath(), processor);
    generator.write(processor.getResource(), outputDirectory.toPath().resolve(outputContextPath), filename);
  }
}
