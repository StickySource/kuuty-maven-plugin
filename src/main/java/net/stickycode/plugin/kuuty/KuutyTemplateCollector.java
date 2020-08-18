package net.stickycode.plugin.kuuty;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isReadable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public class KuutyTemplateCollector {

  public void processSourceDirectory(Path sourceDirectory, KuutyTemplateProcessor configuration) {
    if (!isDirectory(sourceDirectory))
      throw new KuutySourceDirectoryIsMissingFailure(sourceDirectory);

    if (!isReadable(sourceDirectory))
      throw new KuutyCouldNotReadTheSourceDirectoryFailure(sourceDirectory);

    try {
      Files.walk(sourceDirectory)
        .filter(Files::isReadable)
        .filter(Files::isRegularFile)
        .forEach(f -> processFile(configuration, f));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  protected void processFile(KuutyTemplateProcessor configuration, Path f) {
    String name = f.getFileName().toString();
    try {
      String body = Files.readString(f);
      configuration.processTemplate(name, body);
    }
    catch (IOException e) {
      throw new KuutyFailedToReadTemplateFile(e, name);
    }
  }
}
