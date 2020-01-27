package com.example.Projekt2.Service;
import com.example.Projekt2.Domain.PersonState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class PersonStateService {
    @Autowired
    @Qualifier("personState")
    PersonState personState;

    public PersonState getPersonState() {
        return personState;
    }
}
