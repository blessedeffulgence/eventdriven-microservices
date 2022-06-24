package com.eventdriven.microservices.elastic.index.service;

import com.eventdriven.microservices.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient <T extends IndexModel>  {
    List<String> save(List<T> documents);
}
