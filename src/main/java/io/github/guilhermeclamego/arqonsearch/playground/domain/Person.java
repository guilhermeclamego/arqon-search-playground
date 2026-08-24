package io.github.guilhermeclamego.arqonsearch.playground.domain;

public record Person(
        String id,
        String name,
        String email,
        Integer age,
        String city,
        String country
) {
}
