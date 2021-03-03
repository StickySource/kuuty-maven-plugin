# Kuuty Maven Plugin

Generate Kubernetes configs from sample files, just like what a developer would use to test with locally.

Given some sample config files
```
src/main/config/one.properties
src/main/config/three-with-placeholder.properties
```

with content, one.properties
```
a=value
```

and three-with-placeholder.properties
```
a=value
b=value
c=${placeholder}
```

then a configuration like this
```
<plugin>
  <groupId>net.stickycode.plugins</groupId>
  <artifactId>kuuty-maven-plugin</artifactId>
  <version>1.4</version>
  <executions>
    <execution>
      <id>generate-config</id>
      <goals>
        <goal>generate-config</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Would create a file target/resources/kubernetes/configmap.yaml
```
apiVersion: v1
data:
  one.properties: |
    a=value
  three-with-placeholder.properties: |
    a=value
    b=value
    c=${placeholder}
kind: ConfigMap
metadata:
  name: kuuty-maven-plugin-sanity
```

### Full configuation with all the defaults

```
<plugin>
  <groupId>net.stickycode.plugins</groupId>
  <artifactId>kuuty-maven-plugin</artifactId>
  <version>1.4</version>
  <executions>
    <execution>
      <id>generate-config</id>
      <goals>
        <goal>generate-config</goal>
      </goals>
      <phase>process-resources</phase>
      <configuration>
        <outputDirectory>${project.build.directory}/resources/kubernetes</outputDirectory>
        <outputContextPath></outputContextPath>
        <sourceDirectory>src/main/config</sourceDirectory>
        <filename>configmap.yaml</fileName>
        <name>${project.artifactId}</name>
        <namespace>${environment}</name>
      </configuration>
    </execution>
    <execution>
      <id>generate-secret</id>
      <goals>
        <goal>generate-secret</goal>
      </goals>
      <phase>process-resources</phase>
      <configuration>
        <outputDirectory>${project.build.directory}/resources/kubernetes</outputDirectory>
        <outputContextPath></outputContextPath>
        <sourceDirectory>src/main/secrets</sourceDirectory>
        <filename>secret.yaml</fileName>
        <name>${project.artifactId}</name>
        <namespace>${environment}</name>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Output context path

```
<execution>
  <id>generate-config</id>
  <goals>
    <goal>generate-config</goal>
  </goals>
  <phase>process-resources</phase>
</execution>
<execution>
  <id>generate-config-nested</id>
  <goals>
    <goal>generate-config</goal>
  </goals>
  <phase>process-resources</phase>
  <configuration>
    <sourceDirectory>src/main/special-configs</sourceDirectory>
    <outputContextPath>nested/folder</outputContextPath>
  </configuration>
</execution>
```

Notes:
* The outputContextPath is appended to the output directory, useful when you have one execution with nested resources

### Appending to a file

Sometimes you have multiple sources of configuration that need to put the config into one file.

base.properties
```
a=b
```

base.properties.append-extra
```
extra=value
```

Any file named X.append-Y will be appended to the end of X before producing the secret.yaml or configmap.yaml, e.g.
```
a=b
extra=value
```

Something like this will result
```
apiVersion: v1
data:
  one.properties: |
    a=value
    extra=value
kind: ConfigMap
metadata:
  name: kuuty-maven-plugin-sanity
```
