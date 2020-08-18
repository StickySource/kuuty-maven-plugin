package net.stickycode.plugin.kuuty;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import net.stickycode.kuuty.model.v18.IoK8sApiCoreV1ConfigMap;

public class KuutyCreateConfigMapMojoTest {

  @Test
  public void single() throws MojoExecutionException, MojoFailureException {
    IoK8sApiCoreV1ConfigMap other = new IoK8sApiCoreV1ConfigMap();
    other.putDataItem("one.properties", "a=value");
    check("single", other);
  }

  @Test
  public void twoFiles() throws MojoExecutionException, MojoFailureException {
    IoK8sApiCoreV1ConfigMap other = new IoK8sApiCoreV1ConfigMap();
    other.putDataItem("one.properties", "a=value");
    other.putDataItem("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");
    check("twoFiles", other);
  }

  @Test
  public void write() throws IOException {
    KuutyGenerateConfigMapMojo mojo = new KuutyGenerateConfigMapMojo();
    IoK8sApiCoreV1ConfigMap other = mojo.createConfigMap();
    other.putDataItem("one.properties", "a=value");
    other.putDataItem("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");
    Path path = Path.of("target", "config.yaml");
    Files.createDirectories(path.getParent());
    mojo.generateFile(other, path);
  }

  private void check(String example, IoK8sApiCoreV1ConfigMap other) {
    KuutyGenerateConfigMapMojo mojo = new KuutyGenerateConfigMapMojo();
    IoK8sApiCoreV1ConfigMap configmap = mojo.processConfigDirectory(Path.of("src/test/config", example));
    assertThat(configmap).isEqualToComparingFieldByField(other);
  }

}
