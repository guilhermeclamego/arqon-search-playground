package io.github.guilhermeclamego.arqonsearch.playground.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class MongoPersonRepository {

    private final MongoCollection<Document> collection;

    public MongoPersonRepository(MongoDatabase database) {
        this.collection = database.getCollection("person");
    }

    public void saveAll(List<Person> persons) {
        List<Document> documents = persons.stream()
                .map(person -> new Document()
                        .append("id", person.id())
                        .append("name", person.name())
                        .append("email", person.email())
                        .append("age", person.age())
                        .append("city", person.city())
                        .append("country", person.country()))
                .toList();

        collection.insertMany(documents);
    }

    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();

        System.out.println("Collection: " + collection.getNamespace());
        System.out.println("Documents: " + collection.countDocuments());

        collection.find().forEach(document ->
                persons.add(toPerson(document))
        );

        return persons;
    }

    public Person findById(String id) {
        Document document = collection.find(eq("id", id)).first();

        return document != null ? toPerson(document) : null;
    }

    private Person toPerson(Document document) {
        return new Person(
                document.getString("id"),
                document.getString("name"),
                document.getString("email"),
                document.getInteger("age"),
                document.getString("city"),
                document.getString("country")
        );
    }

    public void deleteAll() {
        collection.deleteMany(new Document());
    }
}
