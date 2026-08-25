package io.github.guilhermeclamego.arqonsearch.playground.config;

import io.github.guilhermeclamego.arqonsearch.atlas.AtlasSearchRenderer;
import io.github.guilhermeclamego.arqonsearch.elasticsearch.ElasticsearchSearchRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArqonSearchConfig {

    @Bean
    public ElasticsearchSearchRenderer elasticsearchSearchRenderer() {
        return new ElasticsearchSearchRenderer();
    }

    @Bean
    public AtlasSearchRenderer atlasSearchRenderer() {
        return new AtlasSearchRenderer();
    }
}
