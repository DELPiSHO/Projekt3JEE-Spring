package com.example.Projekt2.Service;


import com.example.Projekt2.Domain.Message;
import com.example.Projekt2.Domain.Person;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReactiveMessage {
    private ConnectionFactory connectionFactory;

    public ReactiveMessage() {
        this.connectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder().host("localhost")
                .username("postgres").password("musia").database("postgres").build());
    }

    public Mono<Message> findOneById(int id) {
        return Mono.from(connectionFactory.create())
                .flatMap(c -> Mono
                        .from(c.createStatement("select * from message where msg_id = $1").bind("$1", id).execute())
                        .doFinally((st) -> close(c)))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("id_person", Integer.class))))
                .flatMap(p -> Mono.from(p));
    }

    public Flux<Message> findAll() {
        return Mono.from(connectionFactory.create()).flatMap(
                (c) -> Mono.from(c.createStatement("select * from message").execute()).doFinally((st) -> close(c)))
                .flatMapMany(result -> Flux.from(result.map((row, meta) -> {
                    Message messages = new Message(row.get("msg_id", Integer.class), row.get("content", String.class),
                            Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                            row.get("id_person", Integer.class));
                    return messages;
                })));
    }

    public Mono<Message> editMessage(int id, String content) {
        return Mono.from(connectionFactory.create()).flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("update message set content = $2 where msg_id = $1 returning *")
                        .bind("$1", id).bind("$2", content).execute()))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("id_person", Integer.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    public Mono<Message> deleteMessage(int id) {
        return Mono.from(connectionFactory.create()).flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("delete from message where msg_id = $1 returning *").bind("$1", id)
                        .execute()))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("id_person", Integer.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    public Mono<Message> addMessage(Message message) {
        return Mono.from(connectionFactory.create()).
                flatMap(c -> Mono.from(c.beginTransaction())
                .then(Mono.from(c.createStatement("insert into message(content,create_date,id_person) values($1,$2,$3)")
                        .bind("$1", message.getContent()).bind("$2", message.getCreateDate()).bind("$3",message.getId_person()).execute()))
                .map(result -> result.map((row, meta) -> new Message(row.get("msg_id", Integer.class),
                        row.get("content", String.class), Timestamp.valueOf((LocalDateTime) row.get("create_date")),
                        row.get("id_person", Integer.class))))
                .flatMap(pub -> Mono.from(pub)).delayUntil(r -> c.commitTransaction()).doFinally((st) -> c.close()));
    }

    private <T> Mono<T> close(Connection connection) {
        return Mono.from(connection.close()).then(Mono.empty());
    }
}

