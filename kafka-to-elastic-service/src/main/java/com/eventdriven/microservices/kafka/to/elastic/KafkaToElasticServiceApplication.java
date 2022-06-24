package com.eventdriven.microservices.kafka.to.elastic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.eventdriven.microservices")
public class KafkaToElasticServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaToElasticServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(KafkaToElasticServiceApplication.class,args);


    }
}
