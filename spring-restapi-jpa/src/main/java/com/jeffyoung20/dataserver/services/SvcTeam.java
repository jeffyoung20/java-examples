package com.jeffyoung20.dataserver.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Team;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.models.dto.TeamDto;
import com.jeffyoung20.dataserver.repos.TeamRepo;

@Component
public class SvcTeam {
	private final static Logger logger = LoggerFactory.getLogger(SvcTeam.class);
	
	@Autowired
	TeamRepo teamRepo;
	
	@Autowired
	SvcPerson svcPerson;
	
	@Autowired
    private ModelMapper modelMapper;
	
	public Team upsert(TeamDto teamDto) {
		List<Team> listMatchingTeams = teamRepo.findByNameIgnoreCase(teamDto.getName());
		Team updTeam = null;
		if(listMatchingTeams.size() == 0) {
			logger.warn(String.format("Inserting Team:  %s", teamDto.getName()));
			updTeam =  modelMapper.map(teamDto, Team.class);
			List<Person> listUpdatedPerson = new ArrayList<Person>();
			if (teamDto.getTeamPersons() != null) {
				for (PersonDto personDto : teamDto.getTeamPersons()) {
					Person updPerson = svcPerson.upsertPerson(personDto);
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
					Person updPerson = svcPerson.upsertPerson(personDto);
					listUpdatedPerson.add(updPerson);
				} 
			}
			updTeam.setTeamPersons(listUpdatedPerson);				
		}
		teamRepo.save(updTeam);
		return updTeam;
	}

}
