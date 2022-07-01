package com.eventdriven.microservices.elastic.query.service.business;

import com.eventdriven.microservices.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.eventdriven.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(String id);
    List<ElasticQueryServiceResponseModel> getDocumentByText(String text);
    List<ElasticQueryServiceResponseModel> getAllDocuments();

}
