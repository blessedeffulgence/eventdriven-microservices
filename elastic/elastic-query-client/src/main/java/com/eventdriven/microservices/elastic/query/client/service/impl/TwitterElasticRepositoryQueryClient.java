package com.eventdriven.microservices.elastic.query.client.service.impl;

import com.eventdriven.microservices.common.util.CollectionsUtil;
import com.eventdriven.microservices.elastic.model.index.impl.TwitterIndexModel;
import com.eventdriven.microservices.elastic.query.client.exception.ElasticQueryClientException;
import com.eventdriven.microservices.elastic.query.client.repository.TwitterElasticSearchQueryRepository;
import com.eventdriven.microservices.elastic.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
@EnableElasticsearchRepositories(basePackages = "com.eventdriven.microservices.elastic.query.client.repository")
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);
    private final TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository;

    public TwitterElasticRepositoryQueryClient(TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository) {
        this.twitterElasticSearchQueryRepository = twitterElasticSearchQueryRepository;
    }


    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Optional<TwitterIndexModel> searchResult = twitterElasticSearchQueryRepository.findById(id);
        LOG.info("Document with id {} retrieved successfully",
                searchResult.orElseThrow(() ->
                        new ElasticQueryClientException("No document found at elasticsearch with id " + id)).getId());
        return searchResult.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        List<TwitterIndexModel> searchResult = twitterElasticSearchQueryRepository.findByText(text);
        LOG.info("{} of documents with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        List<TwitterIndexModel> searchResult =
                CollectionsUtil.getInstance().getListFromIterable(twitterElasticSearchQueryRepository.findAll());
        LOG.info("{} number of documents retrieved successfully", searchResult.size());
        return searchResult;
    }
}
