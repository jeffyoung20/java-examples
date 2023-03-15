package com.jeffyoung20.dataserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeffyoung20.dataserver.exceptions.EntityNotFoundException;
import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Team;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.models.dto.TeamDto;
import com.jeffyoung20.dataserver.services.RepoTeam;
import com.jeffyoung20.dataserver.services.SvcMain;

@RestController
public class TeamController {
	//private final static Logger logger = LoggerFactory.getLogger(TeamController.class);
	
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private RepoTeam repoTeam;
    
    @Autowired 
    private SvcMain svcMain;

    

    @GetMapping("/team")
	public ResponseEntity<List<TeamDto>> getAllTeams() {
		List<Team> listTeams = repoTeam.findAll();
		List<TeamDto> listTeamDtos = listTeams.stream()
			.map(team -> modelMapper.map(team, TeamDto.class))
        	.collect(Collectors.toList());
		return  new ResponseEntity<>(listTeamDtos,HttpStatus.OK);
	}
	
	@GetMapping("/team/{id}")
	public ResponseEntity<TeamDto> getTeamById(@PathVariable("id") long id) {
		Team team = repoTeam.findById(id)
				.orElseThrow(EntityNotFoundException::new); 
		TeamDto teamDto = modelMapper.map(team, TeamDto.class);
		return  new ResponseEntity<>(teamDto, HttpStatus.OK);
	}
	
	@PostMapping("/team")
	public ResponseEntity<List<TeamDto>> addTeams(@RequestBody List<TeamDto> listTeamDto) {
		List<Team> listTeamUpdated = new ArrayList<Team>();
		for(TeamDto teamDto: listTeamDto) {
			Team updTeam = svcMain.saveTeam(teamDto);
			listTeamUpdated.add(updTeam);
		}
		
		//Return updated team by querying database to verify results
		List<Team> returnTeams = new ArrayList<Team>();
		for(Team teamUpdated : listTeamUpdated) {
			Team returnTeam = repoTeam.findById(teamUpdated.getId())
					.orElseThrow(EntityNotFoundException::new); 
			returnTeams.add(returnTeam);	
		}
		List<TeamDto> returnTeamsDto = returnTeams.stream()
			.map(team -> modelMapper.map(team, TeamDto.class))
        	.collect(Collectors.toList());
		return new ResponseEntity<>(returnTeamsDto, HttpStatus.CREATED);
	}

	
	
	@PostMapping("/team/{id}/person")
	public ResponseEntity<TeamDto> addTeamPerson(@PathVariable("id") long teamId, @RequestBody List<PersonDto> listPersonDto) {
		Team team = repoTeam.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
		//List<Person> listPerson = new ArrayList<Person>();
		for(PersonDto personDto: listPersonDto) {
			//Person person = modelMapper.map(personDto, Person.class);
			Person person = svcMain.savePerson(personDto);
			svcMain.addPersonToTeam(person.getId(), team.getId());
		}
		
		//Return Team DTO
		team = repoTeam.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
		TeamDto teamDto = modelMapper.map(team, TeamDto.class);
		return new ResponseEntity<>(teamDto, HttpStatus.CREATED);

	}

	@PutMapping("/team/{teamId}/person/{personId}/new-team/{teamIdNew}")
	public ResponseEntity<List<TeamDto>> movePersonToNewTeam(@PathVariable("teamId") long teamId, 
												@PathVariable("personId") long personId, 
												@PathVariable("teamIdNew") long teamIdNew) {
		svcMain.MovePersonToTeam(teamId, teamIdNew, personId);
		
		//Return Team DTO
		Team teamNew = repoTeam.findById(teamIdNew)
				.orElseThrow(EntityNotFoundException::new); 
		Team teamOld = repoTeam.findById(teamId)
				.orElseThrow(EntityNotFoundException::new); 
		List<TeamDto> listDtos = new ArrayList<TeamDto>();
		listDtos.add(modelMapper.map(teamNew, TeamDto.class));
		listDtos.add(modelMapper.map(teamOld, TeamDto.class));

		return new ResponseEntity<>(listDtos, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/team/{id}")
	public ResponseEntity<HttpStatus> deleteTeam(@PathVariable("id") long id) {
		svcMain.removeAllPersonsFromTeam(id);
		repoTeam.deleteById(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
