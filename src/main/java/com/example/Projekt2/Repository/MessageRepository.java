package com.example.Projekt2.Repository;

import com.example.Projekt2.Domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    <M extends Message> M save(M msg);
    List<Message> findAll();
    List<Message> findByContent(String content);

    void deleteById(int id);



    
}
