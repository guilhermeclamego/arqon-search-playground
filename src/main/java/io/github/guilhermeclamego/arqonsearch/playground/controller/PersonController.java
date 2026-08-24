package io.github.guilhermeclamego.arqonsearch.playground.controller;



import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import io.github.guilhermeclamego.arqonsearch.playground.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping("/mongo")
    public void saveToMongo(@RequestBody Person person) {
        service.saveToMongo(person);
    }

    @PostMapping("/elasticsearch")
    public void saveToElasticsearch(@RequestBody Person person) throws IOException {
        service.saveToElasticsearch(person);
    }

    @GetMapping("/mongo")
    public List<Person> findAllInMongo() {
        return service.findAllInMongo();
    }

    @GetMapping("/elasticsearch")
    public List<Person> findAllInElasticsearch() throws IOException {
        return service.findAllInElasticsearch();
    }

    @GetMapping("/mongo/{id}")
    public Person findByIdInMongo(@PathVariable String id) {
        return service.findByIdInMongo(id);
    }

    @GetMapping("/elasticsearch/{id}")
    public Person findByIdInElasticsearch(@PathVariable String id) throws IOException {
        return service.findByIdInElasticsearch(id);
    }
}