package net.stickycode.plugin.kuuty;

import net.stickycode.kuuty.model.v18.IoK8sApiCoreV1Secret;
import net.stickycode.kuuty.model.v18.IoK8sApimachineryPkgApisMetaV1ObjectMeta;

public class KuutySecretProcessor
    implements KuutyTemplateProcessor<IoK8sApiCoreV1Secret> {

  private final IoK8sApiCoreV1Secret secret;

  public KuutySecretProcessor(String name, String namespace) {
    this.secret = createSecret(name, namespace);
  }

  private IoK8sApiCoreV1Secret createSecret(String name, String namespace) {
    IoK8sApiCoreV1Secret config = new IoK8sApiCoreV1Secret();
    config.setApiVersion("v1");
    config.setKind("Secret");
    config.setType("Opaque");
    IoK8sApimachineryPkgApisMetaV1ObjectMeta metadata = new IoK8sApimachineryPkgApisMetaV1ObjectMeta();
    metadata.setName(name);
    metadata.setNamespace(namespace);
    config.setMetadata(metadata);
    return config;
  }

  @Override
  public void processTemplate(String name, String body) {
    secret.putStringDataItem(name, body);
  }

  @Override
  public IoK8sApiCoreV1Secret getResource() {
    return secret;
  }

}
