package com.example.Projekt2.Service;

import com.example.Projekt2.Domain.CurrentPerson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrentPersonConfig {
    
    @Bean
    public CurrentPerson currentperson(){return new CurrentPerson();}
}
