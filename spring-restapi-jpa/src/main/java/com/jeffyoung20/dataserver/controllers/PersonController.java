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
import org.springframework.web.bind.annotation.RestController;

import com.jeffyoung20.dataserver.models.data.Address;
import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.dto.AddressDto;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.repos.AddressRepo;
import com.jeffyoung20.dataserver.repos.PersonRepo;

@RestController
public class PersonController {

	@Autowired
	PersonRepo personRepo;
	
	@Autowired
	AddressRepo addressRepo;
	
    @Autowired
    private ModelMapper modelMapper;
    
	
	@GetMapping("/person")
	public ResponseEntity<List<PersonDto>> getAllPerson() {
		List<Person> listPersons = personRepo.findAll();
		List<PersonDto> listPersonDtos = listPersons.stream()
			.map(person -> modelMapper.map(person, PersonDto.class))
        	.collect(Collectors.toList());
		return  new ResponseEntity<>(listPersonDtos,HttpStatus.OK);
	}
	
	@GetMapping("/person/{id}")
	public ResponseEntity<PersonDto> getPersonById(@PathVariable("id") long id) {
		Person person = personRepo.findById(id)
				.orElseThrow(RuntimeException::new); //TODO: Change to custom exception type
		PersonDto personDto = modelMapper.map(person, PersonDto.class);
		return  new ResponseEntity<>(personDto, HttpStatus.OK);
	}

	@PostMapping("/person")
	public ResponseEntity<List<PersonDto>> addPersons(@RequestBody List<PersonDto> listPersonDto) {
		List<Person> listPerson = new ArrayList<Person>();
		for(PersonDto personDto: listPersonDto) {
			Person person = modelMapper.map(personDto, Person.class);
			listPerson.add(person);
			for(Address addr: person.getAddresses()) {
				addr.setPerson(person);
			}
		}
		List<Person> listAdded = personRepo.saveAll(listPerson);
		List<PersonDto> listPersonDtos = listAdded.stream()
											.map(person -> modelMapper.map(person, PersonDto.class))
											.collect(Collectors.toList());
		return new ResponseEntity<>(listPersonDtos, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/person/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") long id) {
		personRepo.deleteById(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}