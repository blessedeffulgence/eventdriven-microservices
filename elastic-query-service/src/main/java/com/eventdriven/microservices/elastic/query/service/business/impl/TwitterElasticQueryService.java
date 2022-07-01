package com.eventdriven.microservices.elastic.query.service.business.impl;

import com.eventdriven.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.eventdriven.microservices.elastic.query.client.service.ElasticQueryClient;
import com.eventdriven.microservices.elastic.query.service.business.ElasticQueryService;
import com.eventdriven.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.eventdriven.microservices.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.eventdriven.microservices.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler, ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.elasticQueryServiceResponseModelAssembler = elasticQueryServiceResponseModelAssembler;
        this.elasticQueryClient = elasticQueryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elastic search by id {}",id);
        return elasticQueryServiceResponseModelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying elastic search by text{}",text);
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elastic search");
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }
}