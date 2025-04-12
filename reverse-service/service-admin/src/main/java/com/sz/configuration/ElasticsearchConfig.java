package com.sz.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        RestHighLevelClient client = RestClients.create(ClientConfiguration.localhost()).rest();
        return new ElasticsearchRestTemplate(client);
    }
}
