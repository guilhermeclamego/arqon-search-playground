package io.github.guilhermeclamego.arqonsearch.playground.controller;

import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import io.github.guilhermeclamego.arqonsearch.playground.service.PersonAtlasService;
import io.github.guilhermeclamego.arqonsearch.playground.service.PersonElasticService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonAtlasService atlasService;
    private final PersonElasticService elasticService;

    public PersonController(PersonAtlasService atlasService, PersonElasticService elasticService) {
        this.atlasService = atlasService;
        this.elasticService = elasticService;
    }

    @PostMapping("/mongo")
    public String saveToMongo(@RequestBody List<Person> person) {
        atlasService.saveToMongo(person);
        return "Data saved to MongoDB successfully!";
    }

    @PostMapping("/elasticsearch")
    public String saveToElasticsearch(@RequestBody List<Person> person) throws IOException {
        elasticService.saveToElasticsearch(person);
        return "Data saved to Elasticsearch successfully!";
    }

    @GetMapping("/mongo")
    public List<Person> findAllInMongo() {
        return atlasService.findAllInMongo();
    }

    @GetMapping("/elasticsearch")
    public List<Person> findAllInElasticsearch() throws IOException {
        return elasticService.findAllInElasticsearch();
    }

    @GetMapping("/mongo/{id}")
    public Person findByIdInMongo(@PathVariable String id) {
        return atlasService.findByIdInMongo(id);
    }

    @GetMapping("/elasticsearch/{id}")
    public Person findByIdInElasticsearch(@PathVariable String id) throws IOException {
        return elasticService.findByIdInElasticsearch(id);
    }

    @GetMapping("/elasticsearch/search")
    public List<Person> searchInElasticSearch(@RequestParam String value) throws IOException {
        return elasticService.searchInElasticsearch(value);
    }

    @GetMapping("/mongo/search")
    public List<Person> searchInAtlasSearch(@RequestParam String value) {
        return atlasService.searchInAtlasSearch(value);
    }

    @DeleteMapping("/mongo")
    public String deleteAllInMongo() {
        atlasService.deleteAllInMongo();
        return "All data deleted from MongoDB!";
    }

    @DeleteMapping("/elasticsearch")
    public String deleteAllInElasticsearch() throws IOException {
        elasticService.deleteAllInElasticsearch();
        return "All data deleted from Elasticsearch!";
    }
}