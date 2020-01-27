package com.example.Projekt2.Domain;

import lombok.Data;


@Data
public class CurrentPerson {
    private Person currentPerson;
    public CurrentPerson() {
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }
}
