package com.example.Projekt2.Controller;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.Person;
import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    @Qualifier("personState")
    private PersonState personState;

    @Autowired
    @Qualifier("currentperson")
    private CurrentPerson currentPerson;

    @GetMapping
    public String account(Model model) {
        if (personState.isLogged()){
            model.addAttribute("nick", currentPerson.getCurrentPerson().getNick());
            model.addAttribute("email", currentPerson.getCurrentPerson().getEmail());
            model.addAttribute("admin", personState.isAdmin());
            return "account";
        }
        else{
            return "redirect:/loginForm";
        }
    }
    /*
    @GetMapping("/edit")
    public String editPerson( Model model) {
        model.addAttribute("person", currentPerson.getCurrentPerson());
        return "accountEdit";
    }
    @PostMapping("/replace")
    public String replacePerson(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "accountEdit";
        }
        Person edited = personRepository.getOne(person.getId());
        edited.setNick(person.getNick());
        edited.setPassword(person.getPassword());
        edited.setEmail(person.getEmail());
        personRepository.save(edited);
        currentPerson.setCurrentPerson(edited);
        model.addAttribute("nick", currentPerson.getCurrentPerson().getNick());
        model.addAttribute("email", currentPerson.getCurrentPerson().getEmail());
        return "account";
    }

     */
    @GetMapping("/delete")
    public String deletePerson() {
        Person toDelete = currentPerson.getCurrentPerson();
        personRepository.delete(toDelete);
		personState.setAdmin(false);
        personState.setLogged(false);
        currentPerson.setCurrentPerson(null);
        return "home";
    }
    @GetMapping("/logout")
    public String logout() {
        personState.setAdmin(false);
        personState.setLogged(false);
        currentPerson.setCurrentPerson(null);
        return "home";
    }
    @GetMapping("/info/{id}")
    public String infoPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personRepository.getOne(id));
        return "accountInfo";
    }

}
