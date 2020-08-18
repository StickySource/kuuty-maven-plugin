package net.stickycode.plugin.kuuty;

import java.nio.file.Path;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class KuutySourceDirectoryIsMissingFailure
    extends PermanentException {

  public KuutySourceDirectoryIsMissingFailure(Path directory) {
    super ("Could not find the source directory {} to load template files from", directory);
  }

}
