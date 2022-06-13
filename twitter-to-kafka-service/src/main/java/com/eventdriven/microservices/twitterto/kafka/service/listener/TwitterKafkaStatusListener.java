package com.eventdriven.microservices.twitterto.kafka.service.listener;

import com.eventdriven.microservices.common.config.KafkaConfigData;
import com.eventdriven.microservices.kafka.avro.model.TwitterAvroModel;
import com.eventdriven.microservices.kafka.producer.config.service.KafkaProducer;
import com.eventdriven.microservices.twitterto.kafka.service.transformer.TwitterStatusToAlvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;
@Component
public class TwitterKafkaStatusListener extends StatusAdapter {
private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

private final KafkaConfigData kafkaConfigData;
private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
private final TwitterStatusToAlvroTransformer twitterStatusToAlvroTransformer;

    public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData, KafkaProducer<Long, TwitterAvroModel> kafkaProducer, TwitterStatusToAlvroTransformer twitterStatusToAlvroTransformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.twitterStatusToAlvroTransformer = twitterStatusToAlvroTransformer;
    }

    @Override
    public void onStatus(Status status) {
        LOG.info("Twitter status with text {}" + status.getText(),kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = twitterStatusToAlvroTransformer.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(),twitterAvroModel.getUserId(),twitterAvroModel);
    }
}
