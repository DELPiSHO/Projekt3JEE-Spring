package com.example.Projekt2.Controller;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.Message;
import com.example.Projekt2.Domain.Person;
import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Service.ReactiveMessage;
import com.example.Projekt2.Service.ReactivePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@RestController
@RequestMapping("/rest")
public class ReactiveRestController {
    ReactivePerson reactivePerson;
    ReactiveMessage reactiveMessage;

    @Autowired
    @Qualifier("personState")
    private PersonState personState;

    @Autowired
    @Qualifier("currentperson")
    private CurrentPerson currentPerson;

    public ReactiveRestController() {
        this.reactivePerson = new ReactivePerson();
        this.reactiveMessage = new ReactiveMessage();
    }

    @GetMapping("persons")
    public Flux<Person> showAllPersons() {
        if (personState.isAdmin())
            return reactivePerson.findAll();
        else
            return null;
    }

    @GetMapping("person")
    public Mono<Person> showPerson(@RequestParam("id") int id) {
        if (personState.isAdmin())
            return reactivePerson.findOneById(id);
        else
            return null;
    }

    @GetMapping("EditNick")
    public Mono<Person> changeNick(@RequestParam("id") int id, @RequestParam("nick") String nick) {
        if (personState.isAdmin())
            return reactivePerson.changeNickname(id, nick);
        else
            return null;
    }

    @GetMapping("deletePerson")
    public Mono<Person> deletePerson(@RequestParam("id") int id) {
        if (personState.isAdmin())
            return reactivePerson.deletePerson(id);
        else
            return null;
    }

    @GetMapping("addPerson")
    public Mono<Person> addPerson(@RequestParam("nick") String nick, @RequestParam("email") String email, @RequestParam("password") String password) {
        if (personState.isAdmin()) {
            Person newPerson = new Person();
            newPerson.setNick(nick);
            newPerson.setEmail(email);
            newPerson.setPassword(password);
            return reactivePerson.addPerson(newPerson);
        } else
            return null;
    }

    @GetMapping("messages")
    public Flux<Message> showAllMessages() {
        if (personState.isAdmin())
            return reactiveMessage.findAll();
        else
            return null;
    }

    @GetMapping("message")
    public Mono<Message> showMessage(@RequestParam("id") int id) {
        if (personState.isAdmin())
            return reactiveMessage.findOneById(id);
        else
            return null;
    }

    @GetMapping("editMessage")
    public Mono<Message> editMessage(@RequestParam("id") int id, @RequestParam("content") String content) {
        if (personState.isAdmin())
            return reactiveMessage.editMessage(id, content);
        else
            return null;
    }

    @GetMapping("deleteMessage")
    public Mono<Message> deleteMessage(@RequestParam("id") int id) {
        if (personState.isAdmin())
            return reactiveMessage.deleteMessage(id);
        else
            return null;
    }


    @GetMapping("newMessage")
    public Mono<Message> newMessage(@RequestParam("content") String content, @RequestParam("createDate")Timestamp createDate,@RequestParam("id_person") int id_person){
        if(personState.isAdmin()){
            Message newMessage = new Message();
            newMessage.setContent(content);
            newMessage.setCreateDate(createDate);
            newMessage.setId_person(id_person);
            return reactiveMessage.addMessage(newMessage);
        }
        else
            return null;
    }


}