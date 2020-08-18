package net.stickycode.plugin.kuuty;

import java.io.IOException;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class KuutyFailedToReadTemplateFile
    extends PermanentException {

  public KuutyFailedToReadTemplateFile(IOException e, String name) {
    super(e, "Could not load the template file {} for collection", name);
  }

}
