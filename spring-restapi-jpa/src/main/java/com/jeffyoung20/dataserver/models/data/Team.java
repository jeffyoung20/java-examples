package com.jeffyoung20.dataserver.models.data;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long id;
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "team_person", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
	private List<Person> teamPersons;

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

	public List<Person> getTeamPersons() {
		return teamPersons;
	}

	public void setTeamPersons(List<Person> teamPersons) {
		this.teamPersons = teamPersons;
	}

}
