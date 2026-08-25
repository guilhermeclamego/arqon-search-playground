package io.github.guilhermeclamego.arqonsearch.playground.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Getter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ElasticSearchConfig {

    @Value("${playground.elasticsearch.host}")
    private String host;

    @Value("${playground.elasticsearch.port}")
    private int port;

    @Value("${playground.elasticsearch.scheme:http}")
    private String scheme;

    @Bean(destroyMethod = "close")
    public RestClient restClient() {
        return RestClient.builder(new HttpHost(host, port, scheme)).build();
    }

    @Bean(destroyMethod = "close")
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}