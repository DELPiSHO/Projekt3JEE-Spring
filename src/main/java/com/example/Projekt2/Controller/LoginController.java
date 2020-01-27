package com.example.Projekt2.Controller;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.PersonLogin;
import com.example.Projekt2.Domain.Person;
import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/loginForm")
public class LoginController {

    @Autowired
    @Qualifier("personState")
    private PersonState personState;
    @Autowired
    @Qualifier("currentperson")
    private CurrentPerson currentPerson;

    @Autowired
    PersonRepository personRepository;

    @GetMapping
    public String login(Model model){
        model.addAttribute("PersonLogin", new PersonLogin());
        return "loginForm";
    }

    @PostMapping
    public String validation(@Valid @ModelAttribute("PersonLogin") PersonLogin personLogin, BindingResult result, Model model) {
        model.addAttribute("PersonLogin", new PersonLogin());
        if(result.hasErrors()){
            return "loginForm";
        }
        Person person = personRepository.findByNick(personLogin.getNick());
        if(person!=null){
            if(person.getPassword().equals(personLogin.getPassword())){
                personState.setLogged(true);
                if(person.getId()==1){
                    personState.setAdmin(true);
                }
                currentPerson.setCurrentPerson(person);
                return "redirect:/account";
            }
        }
        return "loginForm";
    }
}
