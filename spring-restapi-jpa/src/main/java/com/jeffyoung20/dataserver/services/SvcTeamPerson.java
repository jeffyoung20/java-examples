package com.jeffyoung20.dataserver.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffyoung20.dataserver.exceptions.EntityNotFoundException;
import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Team;
import com.jeffyoung20.dataserver.repos.PersonRepo;
import com.jeffyoung20.dataserver.repos.TeamRepo;

@Component
public class SvcTeamPerson {
	@Autowired
	private PersonRepo personRepo;
	
	@Autowired
	private TeamRepo teamRepo;


	public void removePersonFromAllTeams(long personId) {
		Person personToDelete = personRepo.findById(personId)
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
			teamRepo.save(team);
		}
		
		//***** DELETE from Person side of link table *****
		personToDelete.setTeams(new ArrayList<Team>());
		personRepo.save(personToDelete);
	}
	
	public void removePersonFromTeamById(long personId, long teamId) {
		Person personToRemove = personRepo.findById(personId)
				.orElseThrow(EntityNotFoundException::new); 
		Team teamToRemoveFrom = teamRepo.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
		
		//***** DELETE from Teams side of link table *****
		List<Person> teamPersons = teamToRemoveFrom.getTeamPersons();
		for(Person per: teamPersons) {
			if(per.getId() == personToRemove.getId()) {
				teamPersons.remove(per);
				teamRepo.save(teamToRemoveFrom);
				break;
			}
		}
		
		//***** DELETE from Person side of link table *****
		List<Team> personTeams = personToRemove.getTeams();
		for(Team team: personTeams) {
			if(team.getId() == teamToRemoveFrom.getId()) {
				personTeams.remove(team);
				personRepo.save(personToRemove);
			}
		}
	}

	public void removeAllPersonsFromTeam(long teamId) {
		Team teamToRemove = teamRepo.findById(teamId)
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
			personRepo.save(person);
		}	
		
		//***** DELETE from Teams side of link table *****
		teamToRemove.setTeamPersons(new ArrayList<Person>());
		teamRepo.save(teamToRemove);
	}

	
	public void addPersonToTeam(long personId, long teamId) {
		Person person = personRepo.findById(personId)
				.orElseThrow(EntityNotFoundException::new); 
		Team team = teamRepo.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
				List<Person> teamPersons = team.getTeamPersons();
				
		if(teamPersons == null) {
			List<Person> listPersons = new ArrayList<Person>();
			team.setTeamPersons(listPersons);
		}
		if(!teamPersons.contains(person)) {
			teamPersons.add(person);
			teamRepo.save(team);
		}

		List<Team> personTeams = person.getTeams();
		if (personTeams == null) {
			personTeams = new ArrayList<Team>();
		}
		if(!personTeams.contains(team)) {
			personTeams.add(team);
			personRepo.save(person);		
		}
	}
}
