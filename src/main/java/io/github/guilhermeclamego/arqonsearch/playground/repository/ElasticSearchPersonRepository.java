package io.github.guilhermeclamego.arqonsearch.playground.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchPersonRepository {

    private static final String INDEX = "person";

    private final ElasticsearchClient client;

    public ElasticSearchPersonRepository(ElasticsearchClient client) {
        this.client = client;
    }

    public void save(Person person) throws IOException {
        client.index(request -> request
                .index(INDEX)
                .id(person.id())
                .document(person)
        );
    }

    public Person findById(String id) throws IOException {
        var response = client.get(request -> request
                .index(INDEX)
                .id(id), Person.class);

        return response.found()
                ? response.source()
                : null;
    }

    public List<Person> findAll() throws IOException {
        var response = client.search(request -> request
                        .index(INDEX)
                        .query(query -> query
                                .matchAll(matchAll -> matchAll)
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