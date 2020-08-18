package net.stickycode.plugin.kuuty;

import java.io.IOException;
import java.nio.file.Path;

import net.stickycode.exception.PermanentException;


@SuppressWarnings("serial")
public class KuutyFailedToGenerateResourceFile
    extends PermanentException {

  public KuutyFailedToGenerateResourceFile(IOException e, Path outputPath) {
    super(e, "Failed to create the resource file {}", outputPath);
  }

}
