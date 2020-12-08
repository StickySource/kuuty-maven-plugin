package net.stickycode.plugin.kuuty;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class PathNameMatchesFilter
    implements Predicate<Path> {

  private Pattern pattern;

  public PathNameMatchesFilter(String regularExpression) {
    this.pattern = Pattern.compile(regularExpression);
  }

  @Override
  public boolean test(Path t) {
    String name = t.getFileName().toString();
    return pattern.matcher(name).matches();
  }

}
