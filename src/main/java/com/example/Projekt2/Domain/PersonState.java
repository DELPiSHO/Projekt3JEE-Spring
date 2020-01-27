package com.example.Projekt2.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PersonState {
    private boolean admin;
    private boolean logged;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isLogged() {
        return logged;
    }
}
