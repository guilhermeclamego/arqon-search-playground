package io.github.guilhermeclamego.arqonsearch.playground.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.guilhermeclamego.arqonsearch.elasticsearch.ElasticsearchSearchRenderer;
import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchPersonRepository {

    private static final String INDEX = "person";

    private final ElasticsearchClient client;
    private final ElasticsearchSearchRenderer renderer;

    public ElasticSearchPersonRepository(
            ElasticsearchClient client,
            ElasticsearchSearchRenderer renderer
    ) {
        this.client = client;
        this.renderer = renderer;
    }

    public void saveAll(List<Person> persons) throws IOException {
        BulkRequest.Builder bulk = new BulkRequest.Builder();

        for (Person person : persons) {
            bulk.operations(operation -> operation
                    .index(index -> index
                            .index(INDEX)
                            .id(person.id())
                            .document(person)
                    )
            );
        }

        client.bulk(bulk.build());
    }

    public Person findById(String id) throws IOException {
        var response = client.get(request -> request
                        .index(INDEX)
                        .id(id),
                Person.class
        );

        return response.found()
                ? response.source()
                : null;
    }

    public List<Person> findAll() throws IOException {
        SearchQuery searchQuery = new SearchQuery(
                List.of(),
                List.of(),
                null
        );

        List<Query> queries = renderer.render(searchQuery);

        var response = client.search(request -> request
                        .index(INDEX)
                        .query(query -> query
                                .bool(bool -> bool
                                        .must(queries)
                                )
                        ),
                Person.class
        );

        return response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }

    public void deleteAll() throws IOException {
        client.deleteByQuery(request -> request
                .index(INDEX)
                .query(query -> query
                        .matchAll(matchAll -> matchAll)
                )
        );
    }

    public List<Person> search(SearchQuery searchQuery) throws IOException {
        List<Query> queries = renderer.render(searchQuery);

        var response = client.search(request -> request
                        .index(INDEX)
                        .query(query -> query
                                .bool(bool -> bool
                                        .must(queries)
                                )
                        ),
                Person.class
        );

        return response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .toList();
    }
}