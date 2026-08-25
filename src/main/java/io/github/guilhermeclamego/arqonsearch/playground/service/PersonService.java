package io.github.guilhermeclamego.arqonsearch.playground.service;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import io.github.guilhermeclamego.arqonsearch.playground.repository.ElasticSearchPersonRepository;
import io.github.guilhermeclamego.arqonsearch.playground.repository.MongoPersonRepository;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PersonService {

    private final MongoPersonRepository mongoRepository;
    private final ElasticSearchPersonRepository elasticsearchRepository;

    public PersonService(
            MongoPersonRepository mongoRepository,
            ElasticSearchPersonRepository elasticsearchRepository
    ) {
        this.mongoRepository = mongoRepository;
        this.elasticsearchRepository = elasticsearchRepository;
    }

    public void saveToMongo(List<Person> person) {
        mongoRepository.saveAll(person);
    }

    public void saveToElasticsearch(List<Person> person) throws IOException {
        elasticsearchRepository.saveAll(person);
    }

    public Person findByIdInMongo(String id) {
        return mongoRepository.findById(id);
    }

    public Person findByIdInElasticsearch(String id) throws IOException {
        return elasticsearchRepository.findById(id);
    }

    public List<Person> findAllInMongo() {
        return mongoRepository.findAll();
    }

    public List<Person> findAllInElasticsearch() throws IOException {
        return elasticsearchRepository.findAll();
    }

    public void deleteAllInMongo() {
        mongoRepository.deleteAll();
    }

    public void deleteAllInElasticsearch() throws IOException {
        elasticsearchRepository.deleteAll();
    }

    public List<Person> searchInElasticsearch(String value) throws IOException {
        SearchQuery query = new SearchQuery(
                List.of(
                        BooleanClause.should(
                                new TextClause(
                                        new Field("name"),
                                        value
                                ),
                                new TextClause(
                                        new Field("email"),
                                        value
                                )
                        )
                ),
                List.of(),
                null
        );

        return elasticsearchRepository.search(query);
    }
}