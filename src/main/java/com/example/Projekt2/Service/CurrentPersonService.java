package com.example.Projekt2.Service;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.Person;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrentPersonService {
    @Autowired
    @Qualifier("currentperson")
    CurrentPerson currentPerson;

    CurrentPerson getCurrentPerson() {
        return currentPerson;
    }

}
