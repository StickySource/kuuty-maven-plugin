package net.stickycode.plugin.kuuty;

import static org.assertj.core.api.Assertions.assertThat;

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
  public void writeConfig() throws IOException {
    KuutyConfigMapProcessor other = new KuutyConfigMapProcessor("theconfig");
    other.processTemplate("one.properties", "a=value");
    other.processTemplate("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");

    Path target = Path.of("target");
    Files.createDirectories(target);
    new KuutyResourceGenerator().write(other.getResource(), target, "config.yaml");
    assertThat(target.resolve("config.yaml")).hasContent("apiVersion: v1\n" +
      "data:\n" +
      "  one.properties: a=value\n" +
      "  some.xml: |\n" +
      "    <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "    <configuration>\n" +
      "      <root level=\"info\" />\n" +
      "    </configuration>\n" +
      "kind: ConfigMap\n" +
      "metadata:\n" +
      "  name: theconfig\n");
  }

  @Test
  public void writeSecret() throws IOException {
    KuutySecretProcessor other = new KuutySecretProcessor("thesecret");
    other.processTemplate("one.properties", "a=value");
    other.processTemplate("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");

    Path target = Path.of("target");
    Files.createDirectories(target);
    new KuutyResourceGenerator().write(other.getResource(), target, "secret.yaml");
    assertThat(target.resolve("secret.yaml")).hasContent("apiVersion: v1\n" +
      "kind: Secret\n" +
      "metadata:\n" +
      "  name: thesecret\n" +
      "stringData:\n" +
      "  one.properties: a=value\n" +
      "  some.xml: |\n" +
      "    <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "    <configuration>\n" +
      "      <root level=\"info\" />\n" +
      "    </configuration>\n" +
      "type: Opaque\n");

  }

}
