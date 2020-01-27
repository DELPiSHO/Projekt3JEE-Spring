package com.example.Projekt2.Domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Table(name="personLogin")
@Data
public class PersonLogin {
    @NotNull
    @Size(min = 2,max = 20,message = "Nickname should be between 2 and 20 characters long")
    private String nick;

    @NotNull
    @Size(min = 5,max = 20,message = "password should be between 5 and 20 characters long")
    private String password;
    public PersonLogin() {
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
