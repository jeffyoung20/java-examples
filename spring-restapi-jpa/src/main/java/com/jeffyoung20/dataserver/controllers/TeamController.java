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
import com.jeffyoung20.dataserver.models.dto.AddressDto;
import com.jeffyoung20.dataserver.models.dto.PersonDto;
import com.jeffyoung20.dataserver.models.dto.TeamDto;
import com.jeffyoung20.dataserver.repos.AddressRepo;
import com.jeffyoung20.dataserver.repos.PersonRepo;
import com.jeffyoung20.dataserver.repos.TeamRepo;

@RestController
public class TeamController {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private TeamRepo teamRepo;
    
    @Autowired
    private PersonRepo personRepo;
    

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
	public ResponseEntity<List<TeamDto>> addTeams(@RequestBody List<TeamDto> listTeamDto) {
		List<Team> listTeamUpdated = new ArrayList<Team>();
		for(TeamDto teamDto: listTeamDto) {
			Team team =  modelMapper.map(teamDto, Team.class);
			List<Person> listPerson = new ArrayList<Person>();
			for(PersonDto personDto: teamDto.getTeamPersons()) {
				Person person = new Person();
				person.setFirstName(personDto.getFirstName());
				person.setLastName(personDto.getLastName());
				List<Address> listAddress = new ArrayList<Address>();
				for(AddressDto addrDto: personDto.getAddresses()) {
					Address addr = new Address();
					addr.setLine1(addrDto.getLine1());
					addr.setLine2(addrDto.getLine2());
					addr.setCity(addrDto.getCity());
					addr.setState(addrDto.getState());
					addr.setZip(addrDto.getZip());
					addr.setPerson(person);
					listAddress.add(addr);
				}
				person.setAddresses(listAddress);
				personRepo.save(person);
				List<Team> listTeams = new ArrayList<Team>();
				listTeams.add(team);
				person.setTeams(listTeams);
				listPerson.add(person);
			}
			team.setTeamPersons(listPerson);
			teamRepo.save(team);
			listTeamUpdated.add(team);
		}
		
		//Return updated team by querying datbase to verify results
		List<Team> returnTeams = new ArrayList<Team>();
		for(Team teamUpdated : listTeamUpdated) {
			Team returnTeam = teamRepo.findById(teamUpdated.getId())
					.orElseThrow(RuntimeException::new); //TODO: Change to custom exception type
			returnTeams.add(returnTeam);	
		}
		List<TeamDto> returnTeamsDto = returnTeams.stream()
			.map(team -> modelMapper.map(team, TeamDto.class))
        	.collect(Collectors.toList());
		return new ResponseEntity<>(returnTeamsDto, HttpStatus.CREATED);
	}
	
	
	@PostMapping("/team/{id}/person")
	public ResponseEntity<TeamDto> addTeamPerson(@PathVariable("id") long teamId, @RequestBody List<PersonDto> listPersonDto) {
		Team team = teamRepo.findById(teamId)
				.orElseThrow(RuntimeException::new); //TODO: Change to custom exception type
		List<Person> listPerson = new ArrayList<Person>();
		for(PersonDto personDto: listPersonDto) {
			Person person = modelMapper.map(personDto, Person.class);
			
			List<Person> teamPersons = team.getTeamPersons();
			if(teamPersons == null) {
				List<Person> listPersons = new ArrayList<Person>();
				team.setTeamPersons(listPersons);
			}
			teamPersons.add(person);
			
			listPerson.add(person);
			for(Address addr: person.getAddresses()) {
				addr.setPerson(person);
			}
			personRepo.save(person);
			teamRepo.save(team);
		}
		
		//Return Team DTO
		team = teamRepo.findById(teamId)
				.orElseThrow(RuntimeException::new); //TODO: Change to custom exception type
		TeamDto teamDto = modelMapper.map(team, TeamDto.class);
		return new ResponseEntity<>(teamDto, HttpStatus.CREATED);

	}

	@DeleteMapping("/team/{id}")
	public ResponseEntity<HttpStatus> deleteTeam(@PathVariable("id") long id) {
		teamRepo.deleteById(id);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
