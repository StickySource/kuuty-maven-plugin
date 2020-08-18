package net.stickycode.plugin.kuuty;

import net.stickycode.kuuty.model.v18.IoK8sApiCoreV1ConfigMap;
import net.stickycode.kuuty.model.v18.IoK8sApimachineryPkgApisMetaV1ObjectMeta;

class KuutyConfigMapProcessor
    implements KuutyTemplateProcessor<IoK8sApiCoreV1ConfigMap> {

  private final IoK8sApiCoreV1ConfigMap configMap;

  KuutyConfigMapProcessor(String name) {
    this.configMap = createConfigMap(name);
  }

  @Override
  public void processTemplate(String name, String body) {
    configMap.putDataItem(name, body);
  }

  public IoK8sApiCoreV1ConfigMap createConfigMap(String name) {
    IoK8sApiCoreV1ConfigMap config = new IoK8sApiCoreV1ConfigMap();
    config.setApiVersion("apps/v1");
    config.setKind("ConfigMap");
    IoK8sApimachineryPkgApisMetaV1ObjectMeta metadata = new IoK8sApimachineryPkgApisMetaV1ObjectMeta();
    metadata.setName(name);
    config.setMetadata(metadata);
    return config;
  }

  @Override
  public IoK8sApiCoreV1ConfigMap getResource() {
    return configMap;
  }
}
