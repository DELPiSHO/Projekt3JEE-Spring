package com.example.Projekt2.Controller;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.Person;
import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.Projekt2.Service.CurrentPersonService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/adminPanel")
public class AdminController {
    @Autowired
    @Qualifier("personState")
    private PersonState personState;
    
    @Autowired
    @Qualifier("currentperson")
    private CurrentPerson currentPerson;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CurrentPersonService currentPersonService;

    public AdminController(CurrentPersonService currentPersonService){
        this.currentPersonService = currentPersonService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        if (personState.isAdmin()){
            model.addAttribute("accounts", personRepository.findAll());
            return "adminPanel";
        }
        else{
            return "redirect:/loginForm";
        }
    }

    @GetMapping("add")
    public String addPerson(Model model){
        model.addAttribute("peson",new Person());
        return "adminPanel";
    }

    @GetMapping("/find")
    public String findPerson(Model model){
        model.addAttribute("person", new Person());
        return "personFind";
    }
    @PostMapping("/findResults")
    public String findResults(Person person, Model model){
        model.addAttribute("accounts", personRepository.findByNick(person.getNick()));
        return "adminPanel";
    }

    @GetMapping("/json/{id}")
    public String JSON(@PathVariable("id") int id, Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(personRepository.findById(id));
        model.addAttribute("JSON",result);
        return "JSON";
    }

    @GetMapping("/edit/{id}")
    public String edytujOsobe(@PathVariable("id") int id, Model model){
        model.addAttribute("person",personRepository.findById(id));
        return "adminEdit";
    }

    @PostMapping("/replace")
    public String replacePerson(@Valid @ModelAttribute("person") Person person, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "adminEdit";
        }
        Person edited = personRepository.getOne(person.getId());
        edited.setNick(person.getNick());
        edited.setPassword(person.getPassword());
        edited.setEmail(person.getEmail());
        personRepository.save(edited);
        currentPerson.setCurrentPerson(edited);
        model.addAttribute("nick", currentPerson.getCurrentPerson().getNick());
        model.addAttribute("email", currentPerson.getCurrentPerson().getEmail());
        return "adminPanel";
    }

    @GetMapping("/csv")
    public void exportCSV(HttpServletResponse response) throws Exception {

        String filename = "persons.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Person> writer = new StatefulBeanToCsvBuilder<Person>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(personRepository.findAll());

    }
}

