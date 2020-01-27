package com.example.Projekt2.Service;

import com.example.Projekt2.Domain.Person;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactivePerson {
    private ConnectionFactory connectionFactory;

    public ReactivePerson() {
        this.connectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder().host("localhost")
                .username("postgres").password("musia").database("postgres").build());
    }

    public Mono<Person> findOneById(int id) {
        return Mono.from(connectionFactory.create())
                .flatMap(c -> Mono.from(
                        c.createStatement("select * from person where person_id = $1").bind("$1", id).execute())
                        .doFinally((st) -> close(c)))
                .map(result -> result.map((row, meta) -> new Person(row.get("person_id", Integer.class),
                        row.get("nick", String.class), row.get("password", String.class),
                        row.get("email", String.class))))
                .flatMap(p -> Mono.from(p));
    }

    public Flux<Person> findAll() {
        return Mono.from(connectionFactory.create()).flatMap(
                (c) -> Mono.from(c.createStatement("select * from person").execute()).doFinally((st) -> close(c)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    Person persons = new Person(row.get("person_id", Integer.class),
                            row.get("nick", String.class), row.get("password", String.class),
                            row.get("email", String.class));
                    return persons;
                })));
    }

    public Mono<Person> addPerson(Person currentperson) {
        return Mono.from(connectionFactory.create())
                .flatMap(c -> Mono.from(c.beginTransaction()).then(Mono.from(c.createStatement(
                        "insert into person(email,nick,password) values($1,$2,$3)")
                        .bind("$1", currentperson.getEmail()).bind("$2", currentperson.getNick()).bind("$3", currentperson.getPassword()).returnGeneratedValues("person_id")
                        .execute()))
                        .map(result -> result.map((row, meta) -> new Person(row.get("person_id", Integer.class),
                                currentperson.getPassword(),currentperson.getNick(),  currentperson.getEmail())))
                        .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction())
                        .doFinally((st) -> c.close()));
    }

    public Mono<Person> deletePerson(int id) {
        return Mono.from(connectionFactory.create()).flatMap(c -> Mono.from(c.beginTransaction()).then(Mono.from(c.createStatement(
                "delete from person where person_id = $1 returning *")
                .bind("$1", id).execute()))
                .map(result -> result.map((row, meta) -> new Person(row.get("person_id", Integer.class),
                        row.get("nick", String.class), row.get("password", String.class),
                        row.get("email", String.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    public Mono<Person> changeNickname(int id, String nickname) {
        return Mono.from(connectionFactory.create()).flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("update person set nick = $2 where person_id = $1 returning *")
                        .bind("$1", id).bind("$2", nickname).execute()))
                .map(result -> result.map((row, meta) -> new Person(row.get("person_id", Integer.class),
                        row.get("nick", String.class), row.get("password", String.class),
                        row.get("email", String.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }



    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close()).then(Mono.empty());
    }
}
