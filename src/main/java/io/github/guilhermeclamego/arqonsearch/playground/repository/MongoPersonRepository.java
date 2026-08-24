package io.github.guilhermeclamego.arqonsearch.playground.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.guilhermeclamego.arqonsearch.playground.domain.Person;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class MongoPersonRepository {

    private final MongoCollection<Document> collection;

    public MongoPersonRepository(MongoDatabase database) {
        this.collection = database.getCollection("person");
    }

    public void save(Person person) {
        Document document = new Document()
                .append("id", person.id())
                .append("name", person.name())
                .append("email", person.email())
                .append("age", person.age())
                .append("city", person.city())
                .append("country", person.country());

        collection.insertOne(document);
    }

    public List<Person> findAll() {
        return collection.find()
                .map(this::toPerson)
                .into(new java.util.ArrayList<>());
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
}
