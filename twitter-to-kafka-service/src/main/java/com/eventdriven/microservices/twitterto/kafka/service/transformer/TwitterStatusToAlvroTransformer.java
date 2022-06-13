package com.eventdriven.microservices.twitterto.kafka.service.transformer;

import com.eventdriven.microservices.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterStatusToAlvroTransformer {

    public TwitterAvroModel getTwitterAvroModelFromStatus(Status status)
    {
        return TwitterAvroModel.newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }
}
