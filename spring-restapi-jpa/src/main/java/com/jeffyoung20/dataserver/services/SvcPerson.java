package com.jeffyoung20.dataserver.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Phone;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.models.dto.PhoneDto;
import com.jeffyoung20.dataserver.repos.PersonRepo;

@Component
public class SvcPerson {
	private final static Logger logger = LoggerFactory.getLogger(SvcPerson.class);
	
	@Autowired
	PersonRepo personRepo;
	
	@Autowired
    private ModelMapper modelMapper;
    

	public Person upsertPerson(PersonDto personDto) {
		List<Person> listMatchingPersons = personRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(personDto.getFirstName(), personDto.getLastName());
		Person personToSave = null;
		if(listMatchingPersons.size() == 0) {
			logger.warn(String.format("Adding Person:  %s %s", personDto.firstName, personDto.lastName));
			personToSave = modelMapper.map(personDto, Person.class);
			for(Phone phone: personToSave.getPhones()) {
				phone.setPerson(personToSave);
			}
			personToSave = personRepo.save(personToSave);
		}
		else {
			logger.warn(String.format("Updating Person:  %s %s", personDto.firstName, personDto.lastName));
			personToSave = listMatchingPersons.get(0);
			personToSave = UpdatePerson(personDto, personToSave);
		}
		return personToSave;
	}
	
	public Person UpdatePerson(PersonDto dtoPersonUpdate, Person existingPerson) {
		List<Phone> updatedPhonesList = existingPerson.getPhones();
		for( PhoneDto phoneDto : dtoPersonUpdate.phones) {
			Phone ph = new Phone();
			ph.setType(phoneDto.getType());
			ph.setNumber(phoneDto.getNumber());
			ph.setPerson(existingPerson);
			updatedPhonesList.add(ph);
		}
		existingPerson.setPhones(updatedPhonesList);
		Person personSaved = personRepo.save(existingPerson);
		return personSaved;
	}
}
