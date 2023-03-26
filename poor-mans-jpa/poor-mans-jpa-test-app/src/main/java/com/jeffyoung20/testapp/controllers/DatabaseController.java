package com.jeffyoung20.testapp.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeffyoung20.testapp.models.ModelPerson;
import com.jeffyoung20.testapp.repos.RepoPerson;



@RestController
@RequestMapping("/api")
public class DatabaseController {
	@Autowired
	private RepoPerson repoPerson;
	
	
	@GetMapping("/person")
	public ResponseEntity<List<ModelPerson>> getPerson(@RequestParam Optional<String> firstName, @RequestParam Optional<String> lastName) throws Exception {
		List<ModelPerson> listPersons = repoPerson.findAll();
		return  new ResponseEntity<>(listPersons, HttpStatus.OK);
	}
	
	@GetMapping("/person/{id}")
	public ResponseEntity<ModelPerson> getPersonById(@PathVariable("id") long id) throws Exception {
		ModelPerson person = repoPerson.findById(id);
		return  new ResponseEntity<>(person, HttpStatus.OK);
	}

	@PostMapping("/person")
	public ResponseEntity<ModelPerson> addPerson(@RequestBody ModelPerson person) {
		repoPerson.save(person);
		return new ResponseEntity<>(person, HttpStatus.CREATED);
	}

	
	@DeleteMapping("/person/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") long id) throws Exception {
		ModelPerson person = repoPerson.findById(id);
		repoPerson.delete(person);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
