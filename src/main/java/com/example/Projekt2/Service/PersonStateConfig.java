package com.example.Projekt2.Service;

import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Domain.PersonState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonStateConfig {
    @Bean
    public PersonState personState(){
        PersonState personState = new PersonState();
        personState.setAdmin(false);
        personState.setLogged(false);
        return personState;
    }
}
