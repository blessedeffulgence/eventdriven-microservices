package com.eventdriven.microservices.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "elastic-query-config")
public class ElasticQueryConfigData {

    private String textField;
}
