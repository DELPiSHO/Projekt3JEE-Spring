package com.example.Projekt2.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.opencsv.bean.*;


import java.util.List;
import java.util.Objects;

@Entity
@Table(name="person")
@Data
public class Person {

    @CsvBindByName(column = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial", name = "person_id")
    private int id;

    @CsvBindByName(column = "nick")
    @Size(min = 3, max = 20, message="Nickname should be between 3 and 24 characters long")
    @Column(unique = true, name="nick")
    @NotNull(message = "Enter your nick")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "Invalid nick")
    private String nick;

    @Size(min = 5, max = 20, message="Password should be between 5 and 20 characters long")
    @Column(name="password")
    @NotNull(message = "Enter your password")
    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "Invalid password")
    private String password;

    @CsvBindByName(column = "email")
    @Column(name="email")
    @NotNull(message ="Please enter your email adress")
    @Pattern(regexp="^(.+)@(.+)$", message = "Please use valid email adress")
    private String email;

    public Person(){}

    public Person(int id, @NotNull String nick, @NotNull String password, @NotNull String email) {
        this.id = id;
        this.nick = nick;
        this.password = password;
        this.email = email;
    }

    public int getId(){return id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return  Objects.equals(nick, person.nick) &&
                Objects.equals(email, person.email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){return password;}

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNick() {
        return nick;
    }
}
