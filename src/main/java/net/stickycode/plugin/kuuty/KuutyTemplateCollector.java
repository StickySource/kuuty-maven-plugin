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
        .filter(new PathNameMatchesFilter(".*\\.append-[a-z0-9-]*").negate())
        .forEach(f -> processFile(configuration, f));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  protected void processFile(KuutyTemplateProcessor configuration, Path f) {
    String name = f.getFileName().toString();
    try {
      StringBuilder body = new StringBuilder(Files.readString(f));
      append(f, body);
      configuration.processTemplate(name, body.toString());
    }
    catch (IOException e) {
      throw new KuutyFailedToReadTemplateFile(e, name);
    }
  }

  private void append(Path f, StringBuilder body) throws IOException {
    Files.walk(f.getParent())
      .filter(Files::isReadable)
      .filter(Files::isRegularFile)
      .filter(new PathNameMatchesFilter(f.getFileName().toString() + "\\.append-[a-z0-9-]*"))
      .forEach(x -> {
        try {
          body.append("\n").append(Files.readString(x));
        }
        catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
  }
}
