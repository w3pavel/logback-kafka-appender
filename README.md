logback-kafka-appender
---

---

这是一个基于Apache Kafka的自定义Logback Appender实现。
主要功能是将日志信息通过自定义的appender发送至Kafka Topic.



# 配置描述

Kafka Producer Record  Key生成策略

| class name                                             | note                                  |
| ------------------------------------------------------ | ------------------------------------- |
| io.coor.logger.logback.kafka.ContextNameKeyingStrategy | 使用ContextName的Hash值作为record key |
| io.coor.logger.logback.kafka.NoKeyKeyingStrategy       | 不使用record key。默认                |



Kafka 发送策略

| class name                                          | note           |
| --------------------------------------------------- | -------------- |
| io.coor.logger.logback.kafka.AnsyncDeliveryStrategy | 异步发送       |
| io.coor.logger.logback.kafka.SyncDeliveryStrategy   | 同步发送。默认 |



样例配置

```xml
<appender name="KAFKA" class="io.coor.logger.logback.KafkaAppender">
    <encoder class="io.coor.logger.logback.encoder.PatternJsonLayoutEncoder">
        <pattern>
            <![CDATA[
                {
                    "timestamp":"%d{yyyy-MM-dd HH:mm:ss.SSS}",
                    "app":"%cn",
                    "thread":"%thread",
                    "level":"%level",
                    "logger":"%logger",
                    "message":"%msg"
                }
            ]]>
        </pattern>
    </encoder>
    <!-- kafka product configuration-->
    <kafkaProduct>
        <!-- target kafka topic -->
        <topic>test</topic>
        <!-- kafka product record key generator strategy -->
        <keyingStrategy class="io.coor.logger.logback.kafka.ContextNameKeyingStrategy"/>
        <!-- kafka product send message strategy -->
        <deliveryStrategy class="io.coor.logger.logback.kafka.AnsyncDeliveryStrategy"/>
        <!-- kafka product config map -->
        <producerConfig>bootstrap.servers=localhost:9092</producerConfig>
        <producerConfig>client.id=${appName}</producerConfig>
    </kafkaProduct>
</appender>
```
