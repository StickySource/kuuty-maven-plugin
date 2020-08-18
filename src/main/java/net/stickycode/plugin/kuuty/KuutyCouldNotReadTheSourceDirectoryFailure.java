package net.stickycode.plugin.kuuty;

import java.nio.file.Path;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class KuutyCouldNotReadTheSourceDirectoryFailure
    extends PermanentException {

  public KuutyCouldNotReadTheSourceDirectoryFailure(Path directory) {
    super("Missing read permission on directory {} to read the template files from", directory);
  }

}
