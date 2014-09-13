# fluentd-appender

log4j 1.XX support.

log4j 2.XX unsupport.


## log4j.xml
```xml:log4j.xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration PUBLIC "-//LOGGER" "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

  <appender name="FLUENTD" class="com.github.toyama0919.logger.sender.appender.FluentAppender">
    <param name="Tag" value="someproject" />
    <param name="Label" value="webapp" />
    <param name="Host" value="localhost" />
    <param name="Port" value="24224" />
    <param name="AddHostname" value="true" />
    <param name="AddTag" value="true" />
  </appender>

  <root>
    <priority value="info" />
    <appender-ref ref="FLUENTD" />
  </root>

</log4j:configuration>
```

## local maven repository install
```bash
gradle clean install uploadArchives
```
