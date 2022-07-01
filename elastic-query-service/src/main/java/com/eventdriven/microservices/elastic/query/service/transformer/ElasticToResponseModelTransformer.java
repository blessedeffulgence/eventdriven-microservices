package com.eventdriven.microservices.elastic.query.service.transformer;

import com.eventdriven.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.eventdriven.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel twitterIndexModel)
    {
        return ElasticQueryServiceResponseModel.builder()
                .id(twitterIndexModel.getId())
                .userId(twitterIndexModel.getUserId())
                .text(twitterIndexModel.getText())
                .createdAt(twitterIndexModel.getCreatedAt().toLocalDateTime())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModel)
    {
        return twitterIndexModel.stream()
                .map(this::getResponseModel).collect(Collectors.toList());
    }
}
