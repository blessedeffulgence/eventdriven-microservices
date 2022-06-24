package com.eventdriven.microservices.kafka.admin.config.client;

import com.eventdriven.microservices.common.config.KafkaConfigData;
import com.eventdriven.microservices.common.config.RetryConfigData;
import com.eventdriven.microservices.kafka.admin.config.exception.KafkaClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaAdminClient {

    private final Logger LOG = LoggerFactory.getLogger(KafkaAdminClient.class);
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData, RetryConfigData retryConfigData, AdminClient adminClient, RetryTemplate retryTemplate, WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;
    }

    public void createTopics()
    {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
        } catch (Throwable e) {
            throw new KafkaClientException("Reached maximum number of retries for creating kafka topic", e);
        }
        checkCreatedTopics();

    }

    public void checkCreatedTopics()
    {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTime = retryConfigData.getSleepTime();
        for (String topic: kafkaConfigData.getTopicNamesToCreate())
        {
            while(! isTopicsCreated(topics, topic))
            {
                checkMaxRetry(retryCount++,maxRetry);
                sleep(sleepTime);
                sleepTime *= multiplier;
                topics = getTopics();
            }
        }

    }

    public void checkSchemaRegistry()
    {
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTime = retryConfigData.getSleepTime();
        while (!getSchemaRegistryStatus().is2xxSuccessful())
        {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTime);
            sleepTime *= multiplier;
        }

    }

    private HttpStatus getSchemaRegistryStatus()
    {
        try {
            return  webClient.method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchangeToMono(response ->{
                        if (response.statusCode().is2xxSuccessful())
                        {
                            return Mono.just(response.statusCode());
                        }
                        else
                        {
                            return Mono.just(HttpStatus.SERVICE_UNAVAILABLE);
                        }
                    })
                    .block();
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void sleep(Long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping for waiting new created topics ");
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry)
        {
            throw  new KafkaClientException("Max retry reached for reading kafka topics");
        }
    }

    private boolean isTopicsCreated(Collection<TopicListing> topics, String topicName) {
        if(topics == null)
        {
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
    }

    private CreateTopicsResult  doCreateTopics(RetryContext retryContext) {
        List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
        LOG.info("Creating {} topics, {} Attempts", topicNames.size(), retryContext.getRetryCount());
        List<NewTopic> kafkaTopics = topicNames.stream()
                .map(topic -> new NewTopic(topic.trim(),
                        kafkaConfigData.getNumberOfPartitions(),
                        kafkaConfigData.getReplicationFactor())).collect(Collectors.toList());
        return adminClient.createTopics(kafkaTopics);
    }

    private Collection<TopicListing> getTopics()
    {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            throw new KafkaClientException("Reached maximum retrys for reading kafka topics",t);
        }
        return topics;

    }

    private Collection <TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        LOG.info("Reading kafka topics{}, attempts {}",kafkaConfigData.getTopicNamesToCreate().toArray(),retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if (topics != null)
        {
            topics.forEach(topic -> LOG.debug("topic with name{}",topic.name()));
        }
        return topics;
    }
}
