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
  <version>1.3-SNAPSHOT</version>
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
apiVersion: apps/v1
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