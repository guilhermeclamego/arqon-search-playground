package io.github.guilhermeclamego.arqonsearch.playground.service;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import io.github.guilhermeclamego.arqonsearch.playground.repository.MongoPersonRepository;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonAtlasService {
    private final MongoPersonRepository mongoRepository;

    public PersonAtlasService(
            MongoPersonRepository mongoRepository
    ) {
        this.mongoRepository = mongoRepository;
    }

    public void saveToMongo(List<Person> person) {
        mongoRepository.saveAll(person);
    }

    public Person findByIdInMongo(String id) {
        return mongoRepository.findById(id);
    }

    public List<Person> findAllInMongo() {
        return mongoRepository.findAll();
    }

    public void deleteAllInMongo() {
        mongoRepository.deleteAll();
    }

    public List<Person> searchInAtlasSearch(String value) {
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

        return mongoRepository.search(query);
    }
}
