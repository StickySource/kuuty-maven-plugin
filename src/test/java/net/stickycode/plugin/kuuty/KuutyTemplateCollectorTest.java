package net.stickycode.plugin.kuuty;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import net.stickycode.kuuty.model.v18.IoK8sApiCoreV1ConfigMap;

public class KuutyTemplateCollectorTest {

  @Test(expected = KuutySourceDirectoryIsMissingFailure.class)
  public void missing() {
    new KuutyTemplateCollector().processSourceDirectory(Path.of("/nonexistent"), null);
  }

  @Test(expected = KuutyCouldNotReadTheSourceDirectoryFailure.class)
  public void cantRead() {
    new KuutyTemplateCollector().processSourceDirectory(Path.of("/root"), null);
  }

  @Test
  public void single() throws MojoExecutionException, MojoFailureException {
    KuutyConfigMapProcessor processor = new KuutyConfigMapProcessor("configname");
    IoK8sApiCoreV1ConfigMap other = processor.getResource();
    other.putDataItem("one.properties", "a=value");
    check("single", other);
  }

  @Test
  public void twoFiles() throws MojoExecutionException, MojoFailureException {
    KuutyConfigMapProcessor processor = new KuutyConfigMapProcessor("configname");
    IoK8sApiCoreV1ConfigMap other = processor.getResource();
    other.putDataItem("one.properties", "a=value");
    other.putDataItem("some.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configuration>\n" +
      "  <root level=\"info\" />\n" +
      "</configuration>\n");
    check("twoFiles", other);
  }

  private void check(String example, IoK8sApiCoreV1ConfigMap other) {
    KuutyConfigMapProcessor processor = new KuutyConfigMapProcessor("configname");

    new KuutyTemplateCollector().processSourceDirectory(Path.of("src/test/config", example), processor);
    assertThat(processor.getResource()).isEqualToComparingFieldByField(other);
  }

}
