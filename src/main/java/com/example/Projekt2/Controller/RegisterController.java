package com.example.Projekt2.Controller;

import com.example.Projekt2.Domain.Person;
import com.example.Projekt2.Repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/registrationForm")
public class RegisterController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public String register(Model model){
        model.addAttribute("person", new Person());
        return "registrationForm";
    }

    @PostMapping
    public String validatePerson(@Valid Person person, Errors errors){
        if(errors.hasErrors()) {
            return "registrationForm";
        }
        personRepository.save(person);
        //SimpleMailMessage mailMessage = new SimpleMailMessage();
        //mailMessage.setTo(person.getEmail());
        //mailMessage.setTo(person.getPassword());
        //mailMessage.setSubject("Registration");
        //mailMessage.setText("Thank you for registration ");
        //javaMailSender.send(mailMessage);
        return "redirect:/loginForm";
    }
}
