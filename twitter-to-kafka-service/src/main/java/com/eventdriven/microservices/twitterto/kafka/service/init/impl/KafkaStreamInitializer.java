package com.eventdriven.microservices.twitterto.kafka.service.init.impl;

import com.eventdriven.microservices.common.config.KafkaConfigData;
import com.eventdriven.microservices.kafka.admin.config.client.KafkaAdminClient;
import com.eventdriven.microservices.twitterto.kafka.service.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamInitializer implements StreamInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamInitializer.class);
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        LOG.info("Topic with name {} is ready for operation",kafkaConfigData.getTopicNamesToCreate().toArray());

    }
}
