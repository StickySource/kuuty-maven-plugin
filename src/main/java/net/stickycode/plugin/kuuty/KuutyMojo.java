package net.stickycode.plugin.kuuty;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.inject.Inject;
import java.io.File;

/**
 * Common properties for kuuty mojo
 */
public abstract class KuutyMojo extends AbstractMojo {
  /**
   * The name of the config/secret resource in Kubernetes
   */
  @Parameter(defaultValue = "${project.name}", required = true)
  String name;

  @Parameter
  String namespace;

  /**
   * Where to write the generated resources
   */
  @Parameter(defaultValue = "${project.build.directory}/resources/kubernetes", required = true)
  File outputDirectory;

  /**
   * The path in the outputDirectory to place the files, useful when you are overriding/embellishing a Software Product or other
   * Kubernetes resources aggregation
   */
  @Parameter
  String outputContextPath = "";

  @Inject
  KuutyTemplateCollector collector;

  @Inject
  KuutyResourceGenerator generator;
}
