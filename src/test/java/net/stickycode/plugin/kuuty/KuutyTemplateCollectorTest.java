package net.stickycode.plugin.kuuty;

import java.nio.file.Path;

import org.junit.Test;

public class KuutyTemplateCollectorTest {

  @Test(expected = KuutySourceDirectoryIsMissingFailure.class)
  public void missing() {
    new KuutyTemplateCollector().verifyConfiguration(new KuutyTemplateCollectorConfiguration() {

      @Override
      public Path getSourceDirectory() {
        return Path.of("/nonexistent");
      }

      @Override
      public Path getOutputDirectory() {
        return null;
      }
    });
  }

  @Test(expected = KuutyCouldNotReadTheSourceDirectoryFailure.class)
  public void cantRead() {
    new KuutyTemplateCollector().verifyConfiguration(new KuutyTemplateCollectorConfiguration() {

      @Override
      public Path getSourceDirectory() {
        return Path.of("/root");
      }

      @Override
      public Path getOutputDirectory() {
        return null;
      }
    });
  }

  @Test(expected = KuutyOutputDirectoryCannotBeCreatedFailure.class)
  public void cantWriteOutputDirectory() {
    new KuutyTemplateCollector().verifyConfiguration(new KuutyTemplateCollectorConfiguration() {

      @Override
      public Path getSourceDirectory() {
        return Path.of("src/test/config");
      }

      @Override
      public Path getOutputDirectory() {
        return Path.of("/root/some/outputdir");
      }
    });
  }

}
