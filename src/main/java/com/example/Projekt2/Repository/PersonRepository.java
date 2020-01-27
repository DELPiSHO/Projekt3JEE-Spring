package com.example.Projekt2.Repository;

import com.example.Projekt2.Domain.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>  {
    <M extends Person> M save(M user);
    List<Person> persons = new ArrayList<>();
    List<Person> findAll();

    Person findByNick(String nick);

    Person findById(int id);

    Person findByEmail(String email);
}
