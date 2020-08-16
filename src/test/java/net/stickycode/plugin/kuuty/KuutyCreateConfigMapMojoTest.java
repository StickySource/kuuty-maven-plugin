package net.stickycode.plugin.kuuty;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.openapitools.client.model.IoK8sApiCoreV1ConfigMap;

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
  public void write() {
    KuutyCreateConfigMapMojo mojo = new KuutyCreateConfigMapMojo();
    IoK8sApiCoreV1ConfigMap other = mojo.createConfigMap();
    other.putDataItem("one.properties", "a=value");
    other.putDataItem("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");
    mojo.generateFile(other, Path.of("target", "config.yaml"));
  }

  private void check(String example, IoK8sApiCoreV1ConfigMap other) {
    KuutyCreateConfigMapMojo mojo = new KuutyCreateConfigMapMojo();
    IoK8sApiCoreV1ConfigMap configmap = mojo.processConfigDirectory(Path.of("src/test/config", example));
    assertThat(configmap).isEqualToComparingFieldByField(other);
  }

}
