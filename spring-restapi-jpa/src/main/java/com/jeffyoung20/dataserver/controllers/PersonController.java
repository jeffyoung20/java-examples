package com.jeffyoung20.dataserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeffyoung20.dataserver.exceptions.EntityNotFoundException;
import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.services.RepoPerson;
import com.jeffyoung20.dataserver.services.SvcMain;

@RestController
public class PersonController {

	@Autowired
	RepoPerson repoPerson;
	
	@Autowired
	SvcMain svcMain;
	
	@Autowired
    private ModelMapper modelMapper;
    
	
	@GetMapping("/person")
	public ResponseEntity<List<PersonDto>> getPerson(@RequestParam Optional<String> firstName, @RequestParam Optional<String> lastName) {
		List<Person> listPersons = new ArrayList<Person>();
		if(firstName.isEmpty() && lastName.isEmpty())
		{
			listPersons = repoPerson.findAll();
		}
		else {
			String fname = firstName.get();
			String lname = lastName.get();
			listPersons = repoPerson.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(fname, lname);
		}
		List<PersonDto> listPersonDtos = listPersons.stream()
			.map(person -> modelMapper.map(person, PersonDto.class))
        	.collect(Collectors.toList());
		return  new ResponseEntity<>(listPersonDtos,HttpStatus.OK);
	}
	
	@GetMapping("/person/{id}")
	public ResponseEntity<PersonDto> getPersonById(@PathVariable("id") long id) {
		Person person = repoPerson.findById(id)
				.orElseThrow(EntityNotFoundException::new); 
		PersonDto personDto = modelMapper.map(person, PersonDto.class);
		return  new ResponseEntity<>(personDto, HttpStatus.OK);
	}

	@PostMapping("/person")
	public ResponseEntity<List<PersonDto>> addPersons(@RequestBody List<PersonDto> listPersonDto) {
		List<Person> listSavedPersons = new ArrayList<Person>();
		for(PersonDto personDto: listPersonDto) {
			Person personToSave = svcMain.savePerson(personDto);
			listSavedPersons.add(personToSave);
		}

		List<PersonDto> listPersonDtos = listSavedPersons.stream()
											.map(person -> modelMapper.map(person, PersonDto.class))
											.collect(Collectors.toList());
		return new ResponseEntity<>(listPersonDtos, HttpStatus.CREATED);
	}

	
	@DeleteMapping("/person/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") long id) {
		svcMain.removePersonFromAllTeams(id);	
		repoPerson.deleteById(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
