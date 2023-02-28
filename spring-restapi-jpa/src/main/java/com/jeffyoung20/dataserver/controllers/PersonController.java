package com.jeffyoung20.dataserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeffyoung20.dataserver.models.Address;
import com.jeffyoung20.dataserver.models.Person;
import com.jeffyoung20.dataserver.repos.AddressRepo;
import com.jeffyoung20.dataserver.repos.PersonRepo;

@RestController
public class PersonController {

	@Autowired
	PersonRepo personRepo;
	
	@Autowired
	AddressRepo addressRepo;
	
	@GetMapping("/person")
	public ResponseEntity<List<Person>> getAllPerson() {
		List<Person> listPersons = personRepo.findAll();
		return  new ResponseEntity<>(listPersons,HttpStatus.OK);
	}
	
	@GetMapping("/person/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable("id") long id) {
		Person p = personRepo.findById(id)
				.orElseThrow(RuntimeException::new); //TODO: Change to custom exception type
		return  new ResponseEntity<>(p, HttpStatus.OK);
	}

	@PostMapping("/person")
	public ResponseEntity<List<Person>> addPersons(@RequestBody List<Person> listPerson) {
		for(Person person: listPerson) {
			for(Address addr : person.addresses) {
				addr.person = person;
			}
		}
		List<Person> listAdded = personRepo.saveAll(listPerson);
		return new ResponseEntity<>(listAdded, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/person/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") long id) {
		personRepo.deleteById(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
