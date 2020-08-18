package net.stickycode.plugin.kuuty;

public interface KuutyTemplateProcessor<T> {

  void processTemplate(String name, String body);

  T getResource();

}
