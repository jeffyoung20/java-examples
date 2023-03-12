package com.jeffyoung20.dataserver.models.dto;

import java.util.List;

public class TeamDto {
	private long id;
	private String name;
	
	private List<PersonDto> teamPersons;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PersonDto> getTeamPersons() {
		return teamPersons;
	}
	public void setTeamPersons(List<PersonDto> teamPersons) {
		this.teamPersons = teamPersons;
	}
}
