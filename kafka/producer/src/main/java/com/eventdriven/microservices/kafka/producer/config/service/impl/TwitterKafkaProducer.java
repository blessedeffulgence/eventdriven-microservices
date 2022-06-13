package com.eventdriven.microservices.kafka.producer.config.service.impl;

import com.eventdriven.microservices.kafka.avro.model.TwitterAvroModel;
import com.eventdriven.microservices.kafka.producer.config.service.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

    private final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<Long,TwitterAvroModel> kafkaTemplate;

    public TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        LOG.info("Sending the message{}, to topic{}",message,topicName);
        ListenableFuture<SendResult<Long, TwitterAvroModel>> listenableFuture =
                kafkaTemplate.send(topicName,key,message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Long, TwitterAvroModel>>() {
            @Override
            public void onSuccess(SendResult<Long, TwitterAvroModel> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                LOG.debug("Received record metadata. Topic{},Partition{},offset{},timestamp{},at_time{}",
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp(),
                        System.nanoTime());

            }

            @Override
            public void onFailure(Throwable ex) {
            LOG.error("Error while sending message{} to topic{}",message.toString(),topicName,ex);
            }
        });

    }

    @PreDestroy
    public void close()
    {
        if (kafkaTemplate != null)
        {
            LOG.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }
}
