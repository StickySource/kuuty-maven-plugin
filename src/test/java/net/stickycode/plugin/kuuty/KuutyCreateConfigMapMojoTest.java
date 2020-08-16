package net.stickycode.plugin.kuuty;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.nio.file.Path;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.openapitools.client.model.IoK8sApiCoreV1ConfigMap;

public class KuutyCreateConfigMapMojoTest {

  @Test
  public void sanity() throws MojoExecutionException, MojoFailureException {
    KuutyCreateConfigMapMojo mojo = new KuutyCreateConfigMapMojo();
    IoK8sApiCoreV1ConfigMap configmap = mojo.processConfigDirectory(Path.of("src/test/config", "sanity"));
    IoK8sApiCoreV1ConfigMap other = new IoK8sApiCoreV1ConfigMap();
    other.putDataItem("one.properties", "a=value");
    assertThat(configmap).isEqualToComparingFieldByField(other);
  }

}
