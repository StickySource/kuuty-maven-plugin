package net.stickycode.plugin.kuuty;

import static java.nio.file.Files.createDirectories;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Named;
import javax.inject.Singleton;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Singleton
@Named
public class KuutyResourceGenerator {

  public void write(Object resource, Path outputDirectory, String filename) {
    Path outputPath = outputDirectory.resolve(filename);
    ensureParentDirectory(outputPath);

    DumperOptions options = new DumperOptions();
    options.setPrettyFlow(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setCanonical(false);
    options.setExplicitStart(false);
    Yaml yaml = new Yaml(new KuutyRepresenter(), options);
    try (BufferedWriter writer = Files.newBufferedWriter(outputPath);) {
      yaml.dump(resource, writer);
    }
    catch (IOException e) {
      throw new KuutyFailedToGenerateResourceFile(e, outputPath);
    }
  }

  private void ensureParentDirectory(Path outputPath) {
    try {
      createDirectories(outputPath.getParent());
    }
    catch (IOException e) {
      throw new KuutyOutputDirectoryCannotBeCreatedFailure(e, outputPath.getParent());
    }
  }
}
