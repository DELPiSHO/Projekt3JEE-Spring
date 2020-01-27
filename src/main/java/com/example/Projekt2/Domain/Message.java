package com.example.Projekt2.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="message")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial", name = "msg_id")
    int id;
    @Size(min = 2, message = "Too little content...")
    @Size(max = 20, message = "This is too much...")
    @NotNull(message = "Content cannot be empty")
    private String content;
    private Timestamp createDate;
    private int id_person;

    public Message() {
        this.createDate = new Timestamp((new java.util.Date().getTime()));
    }



    public Message(int id, @NotEmpty String content, @NotEmpty Timestamp createDate, int id_person) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.id_person = id_person;
    }

    public int getId(){return id;}

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id_person, message.id_person) &&
                Objects.equals(createDate, message.createDate) &&
                Objects.equals(content, message.content);
    }

    
    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId_person() {
        return id_person;
    }

    public void setId_person(int id_person) {
        this.id_person = id_person;
    }

    public Timestamp getCreateDate() {
        return new Timestamp((new java.util.Date().getTime()));
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
