package net.stickycode.plugin.kuuty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class KuutyResourceGeneratorTest {

  @Test(expected = KuutyOutputDirectoryCannotBeCreatedFailure.class)
  public void cantWriteOutputDirectory() {
    new KuutyResourceGenerator().write(null, Path.of("/root/some"), "file");
  }

  @Test
  public void write() throws IOException {
    KuutyConfigMapProcessor other = new KuutyConfigMapProcessor("theconfig");
    other.processTemplate("one.properties", "a=value");
    other.processTemplate("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");

    Files.createDirectories(Path.of("target"));
    new KuutyResourceGenerator().write(other, Path.of("target"), "config.yaml");
  }

}
