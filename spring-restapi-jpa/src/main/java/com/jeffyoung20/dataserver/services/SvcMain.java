package com.jeffyoung20.dataserver.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffyoung20.dataserver.exceptions.EntityNotFoundException;
import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Phone;
import com.jeffyoung20.dataserver.models.data.Team;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.models.dto.PhoneDto;
import com.jeffyoung20.dataserver.models.dto.TeamDto;

@Component
public class SvcMain {
	private final static Logger logger = LoggerFactory.getLogger(SvcMain.class);

	@Autowired
	private RepoPerson repoPerson;
	
	@Autowired
	private RepoTeam repoTeam;
	
	@Autowired
	private RepoPhone repoPhone;

	@Autowired
    private ModelMapper modelMapper;
	
	
	public Person savePerson(PersonDto personDto) {
		List<Person> listMatchingPersons = repoPerson.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(personDto.getFirstName(), personDto.getLastName());
		Person personToSave = null;
		if(listMatchingPersons.size() == 0) {
			logger.warn(String.format("Adding Person:  %s %s", personDto.firstName, personDto.lastName));
			//personToSave = modelMapper.map(personDto, Person.class);
			personToSave = new Person();
			personToSave.setFirstName(personDto.firstName);
			personToSave.setLastName(personDto.lastName);
			List<PhoneDto> listDtoPhones = personDto.getPhones() != null ? personDto.getPhones() : new ArrayList<PhoneDto>();
			List<Phone> listPhones = new ArrayList<Phone>();
			for(PhoneDto phoneDto : listDtoPhones) {
				Phone phone = new Phone();
				phone.setNumber(phoneDto.getNumber());
				phone.setType(phoneDto.getType());
				phone.setPerson(personToSave);
				listPhones.add(phone);
			}
			personToSave.setPhones(listPhones);
			personToSave = repoPerson.save(personToSave);
		}
		else {
			logger.warn(String.format("Updating Person:  %s %s", personDto.firstName, personDto.lastName));
			personToSave = listMatchingPersons.get(0);
			personToSave = updatePerson(personDto, personToSave);
		}
		return personToSave;
	}
	
	public Team saveTeam(TeamDto teamDto) {
		List<Team> listMatchingTeams = repoTeam.findByNameIgnoreCase(teamDto.getName());
		Team updTeam = null;
		if(listMatchingTeams.size() == 0) {
			logger.warn(String.format("Inserting Team:  %s", teamDto.getName()));
			updTeam =  modelMapper.map(teamDto, Team.class);
			List<Person> listUpdatedPerson = new ArrayList<Person>();
			if (teamDto.getTeamPersons() != null) {
				for (PersonDto personDto : teamDto.getTeamPersons()) {
					Person updPerson = savePerson(personDto);
					listUpdatedPerson.add(updPerson);
				} 
			}
			updTeam.setTeamPersons(listUpdatedPerson);
		}
		else {
			logger.warn(String.format("Updating Team:  %s", teamDto.getName()));
			updTeam = listMatchingTeams.get(0);
			List<Person> listUpdatedPerson = updTeam.getTeamPersons();
			if (teamDto.getTeamPersons() != null) {
				for (PersonDto personDto : teamDto.getTeamPersons()) {
					Person updPerson = savePerson(personDto);
					listUpdatedPerson.add(updPerson);
				} 
			}
			updTeam.setTeamPersons(listUpdatedPerson);				
		}
		repoTeam.save(updTeam);
		return updTeam;
	}

	private Person updatePerson(PersonDto dtoPersonUpdate, Person existingPerson) {
		//List<Phone> updatedPhonesList = existingPerson.getPhones();
		List<Phone> updatedPhonesList = new ArrayList<Phone>();
		for( PhoneDto phoneDto : dtoPersonUpdate.phones) {
			Phone ph = new Phone();
			ph.setType(phoneDto.getType());
			ph.setNumber(phoneDto.getNumber());
			ph.setPerson(existingPerson);
			updatedPhonesList.add(ph);
		}
		removeAllPhonesFromPerson( existingPerson );
		existingPerson.setPhones(updatedPhonesList);
		Person personSaved = repoPerson.save(existingPerson);
		return personSaved;
	}

	public void removeAllPhonesFromPerson(Person person) {
		List<Phone> listExistingPhones = person.getPhones();
		for(Phone phone : listExistingPhones) {
			phone.setPerson(null);
			repoPhone.save(phone);
		}
	}
	
 	public void removePersonFromAllTeams(long personId) {
		Person personToDelete = repoPerson.findById(personId)
				.orElseThrow(EntityNotFoundException::new); 
	
		//***** DELETE from Teams side of link table *****
		List<Team> listTeams = personToDelete.getTeams();
		List<Person> teamPersonsToRemove = new ArrayList<Person>();
		for(Team team: listTeams) {
			List<Person> teamPersons = team.getTeamPersons();
			for(Person per: teamPersons) {
				if(per.getId() == personId) {
					teamPersonsToRemove.add(per);
				}
			}
			teamPersons.removeAll(teamPersonsToRemove);
			repoTeam.save(team);
		}
		
		//***** DELETE from Person side of link table *****
		personToDelete.setTeams(new ArrayList<Team>());
		repoPerson.save(personToDelete);
	}
	
	private void removePersonFromTeamById(long personId, long teamId) {
		Person personToRemove = repoPerson.findById(personId)
				.orElseThrow(EntityNotFoundException::new); 
		Team teamToRemoveFrom = repoTeam.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
		
		//***** DELETE from Teams side of link table *****
		List<Person> teamPersons = teamToRemoveFrom.getTeamPersons();
		for(Person per: teamPersons) {
			if(per.getId() == personToRemove.getId()) {
				teamPersons.remove(per);
				repoTeam.save(teamToRemoveFrom);
				break;
			}
		}
		
		//***** DELETE from Person side of link table *****
		List<Team> personTeams = personToRemove.getTeams();
		for(Team team: personTeams) {
			if(team.getId() == teamToRemoveFrom.getId()) {
				personTeams.remove(team);
				repoPerson.save(personToRemove);
			}
		}
	}

	public void removeAllPersonsFromTeam(long teamId) {
		Team teamToRemove = repoTeam.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
		
		//***** DELETE from Persons side of link table *****
		List<Person> listPersonsOnTeam = teamToRemove.getTeamPersons();
		for(Person person: listPersonsOnTeam) {
			List<Team> teamsToRemoveFromPerson = new ArrayList<Team>();
			for(Team team: person.getTeams()) {
				if(team.getId() == teamId) {
					teamsToRemoveFromPerson.add(team);
				}
			}
			person.getTeams().removeAll(teamsToRemoveFromPerson);
			repoPerson.save(person);
		}	
		
		//***** DELETE from Teams side of link table *****
		teamToRemove.setTeamPersons(new ArrayList<Person>());
		repoTeam.save(teamToRemove);
	}

	
	public void addPersonToTeam(long personId, long teamId) {
		Person person = repoPerson.findById(personId)
				.orElseThrow(EntityNotFoundException::new); 
		Team team = repoTeam.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 

		List<Person> teamPersons = team.getTeamPersons();
		if(teamPersons == null) {
			List<Person> listPersons = new ArrayList<Person>();
			team.setTeamPersons(listPersons);
		}
		if(!teamPersons.contains(person)) {
			teamPersons.add(person);
			repoTeam.save(team);
		}

		List<Team> personTeams = person.getTeams();
		if (personTeams == null) {
			personTeams = new ArrayList<Team>();
		}
		if(!personTeams.contains(team)) {
			personTeams.add(team);
			repoPerson.save(person);		
		}
	}

	public void MovePersonToTeam(long teamIdOld, long teamIdNew, long personId) {
		removePersonFromTeamById(personId,teamIdOld);
		addPersonToTeam(personId, teamIdNew);
	}
}
