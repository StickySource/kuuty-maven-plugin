package net.stickycode.plugin.kuuty;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isReadable;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public class KuutyTemplateCollector {

  public void verifyConfiguration(KuutyTemplateCollectorConfiguration configuration) {
    Path sourceDirectory = configuration.getSourceDirectory();
    if (!isDirectory(sourceDirectory))
      throw new KuutySourceDirectoryIsMissingFailure(sourceDirectory);

    if (!isReadable(sourceDirectory))
      throw new KuutyCouldNotReadTheSourceDirectoryFailure(sourceDirectory);

    try {
      createDirectories(configuration.getOutputDirectory());
    }
    catch (IOException e) {
      throw new KuutyOutputDirectoryCannotBeCreatedFailure(e, configuration.getOutputDirectory());
    }
  }
}
