package com.example.Projekt2.Controller;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.Message;
import com.example.Projekt2.Domain.Person;
import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Repository.MessageRepository;
import com.example.Projekt2.Repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/messages")
public class MessagesController {
    @Autowired
    @Qualifier("personState")
    private PersonState personState;
    @Autowired
    @Qualifier("currentperson")
    private CurrentPerson currentPerson;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping
    public String messages(Model model) {
        if (personState.isLogged()){
            model.addAttribute("accounts", personRepository.findAll());
            model.addAttribute("messages", messageRepository.findAll());
            model.addAttribute("currentId", currentPerson.getCurrentPerson().getId());
            return "messages";
        }
        else{
            return "redirect:/loginForm";
        }
    }

    @GetMapping("/new")
    public String newMessage(Model model){
        model.addAttribute("message", new Message());
        return "messageNew";
    }

    @PostMapping("/add")
    public String validateMessage(@Valid Message message, Errors errors){
        if(errors.hasErrors()){
            return "messageNew";
        }

        message.setCreateDate(new Timestamp((new java.util.Date().getTime())));
        message.setId_person(currentPerson.getCurrentPerson().getId());
        messageRepository.save(message);

        return "redirect:/messages";
    }

    @GetMapping("/edit/{id}")
    public String editMessage(@PathVariable("id") int id, Model model) {
        model.addAttribute("message", messageRepository.findById(id));
        return "messageEdit";
    }
    @PostMapping("/replace")
    public String replaceMessage(@Valid @ModelAttribute("message") Message message, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "accountEdit";
        }
        Message edited = messageRepository.getOne(message.getId());
        edited.setContent(message.getContent());
        edited.setCreateDate(new Timestamp((new java.util.Date().getTime())));
        messageRepository.save(edited);
        return "redirect:/messages";
    }
    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") int id, Model model) {
        Message toDelete = messageRepository.getOne(id);
        messageRepository.delete(toDelete);
        return "redirect:/messages";
    }

    @GetMapping("/find")
    public String findMessage(Model model){
        model.addAttribute("message", new Message());
        return "messageFind";
    }
    @PostMapping("/findResults")
    public String findResults(Message message, Model model){
        model.addAttribute("messages", messageRepository.findByContent(message.getContent()));
        return "messages";
    }
}
