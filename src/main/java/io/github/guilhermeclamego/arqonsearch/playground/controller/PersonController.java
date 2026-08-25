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
    public String saveToMongo(@RequestBody List<Person> person) {
        service.saveToMongo(person);
        return "Data saved to MongoDB successfully!";
    }

    @PostMapping("/elasticsearch")
    public String saveToElasticsearch(@RequestBody List<Person> person) throws IOException {
        service.saveToElasticsearch(person);
        return "Data saved to Elasticsearch successfully!";
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

    @GetMapping("/elasticsearch/search")
    public List<Person> searchInElasticsearch(@RequestParam String value) throws IOException {
        return service.searchInElasticsearch(value);
    }

    @DeleteMapping("/mongo")
    public String deleteAllInMongo() {
        service.deleteAllInMongo();
        return "All data deleted from MongoDB!";
    }

    @DeleteMapping("/elasticsearch")
    public String deleteAllInElasticsearch() throws IOException {
        service.deleteAllInElasticsearch();
        return "All data deleted from Elasticsearch!";
    }
}