package net.stickycode.plugin.kuuty;

import java.io.IOException;
import java.nio.file.Path;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class KuutyOutputDirectoryCannotBeCreatedFailure
    extends PermanentException {

  public KuutyOutputDirectoryCannotBeCreatedFailure(IOException e, Path outputDirectory) {
    super(e, "Could not create the output directory {} for the generated Kuberentes resources", outputDirectory);
  }

}
