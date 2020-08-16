package net.stickycode.plugin.kuuty;

import java.util.Set;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class KuutyRepresenter
    extends Representer {

  @Override
  protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
    // if value of property is null, ignore it.
    if (propertyValue == null) {
      return null;
    }
    else {
      return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
    }
  }

  @Override
  protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
      if (!classTags.containsKey(javaBean.getClass()))
          addClassTag(javaBean.getClass(), Tag.MAP);

      return super.representJavaBean(properties, javaBean);
  }

}
