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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jeffyoung20.dataserver.models.data.Address;
import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Team;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.models.dto.TeamDto;
import com.jeffyoung20.dataserver.repos.TeamRepo;

@RestController
public class TeamController {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private TeamRepo teamRepo;
    
	@GetMapping("/team")
	public ResponseEntity<List<TeamDto>> getAllTeams() {
		List<Team> listTeams = teamRepo.findAll();
		List<TeamDto> listTeamDtos = listTeams.stream()
			.map(team -> modelMapper.map(team, TeamDto.class))
        	.collect(Collectors.toList());
		return  new ResponseEntity<>(listTeamDtos,HttpStatus.OK);
	}
	
	@GetMapping("/team/{id}")
	public ResponseEntity<TeamDto> getTeamById(@PathVariable("id") long id) {
		Team team = teamRepo.findById(id)
				.orElseThrow(RuntimeException::new); //TODO: Change to custom exception type
		TeamDto teamDto = modelMapper.map(team, TeamDto.class);
		return  new ResponseEntity<>(teamDto, HttpStatus.OK);
	}
	

	@PostMapping("/team")
	public ResponseEntity<List<TeamDto>> addTeamss(@RequestBody List<TeamDto> listTeamDto) {
		List<Team> listTeam = new ArrayList<Team>();
		for(TeamDto teamDto: listTeamDto) {
			Team team =  modelMapper.map(teamDto, Team.class);
			
//			List<Person> listPersons = new ArrayList<Person>();
//			Person p = new Person("Jeff", "Young");
//			listPersons.add(p);
//			team.setTeamPersons(listPersons);
			
			listTeam.add(team);
		}
		List<Team> listAdded = teamRepo.saveAll(listTeam);
		List<TeamDto> listTeamDtos = listAdded.stream()
											.map(team -> modelMapper.map(team, TeamDto.class))
											.collect(Collectors.toList());
		return new ResponseEntity<>(listTeamDtos, HttpStatus.CREATED);
	}

	@DeleteMapping("/team/{id}")
	public ResponseEntity<HttpStatus> deleteTeam(@PathVariable("id") long id) {
		teamRepo.deleteById(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
