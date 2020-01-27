package com.example.Projekt2;

import com.example.Projekt2.Domain.CurrentPerson;
import com.example.Projekt2.Domain.PersonState;
import com.example.Projekt2.Service.ReactiveMessage;
import com.example.Projekt2.Service.ReactivePerson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Projekt2ApplicationTests {

	@Test
	void contextLoads() {
	}

	ReactivePerson reactivePerson;
	ReactiveMessage reactiveMessage;

	@Autowired
	@Qualifier("personState")
	private PersonState personState;

	@Autowired
	@Qualifier("currentperson")
	private CurrentPerson currentPerson;



}
