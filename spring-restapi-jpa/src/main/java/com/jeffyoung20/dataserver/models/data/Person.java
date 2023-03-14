package com.jeffyoung20.dataserver.models.data;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

//@Data
@Entity
//@Table(name = "person")
public class Person {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	private String firstName;
	private String lastName;
	
	
	/*
	 * NOTE:  mappedBy property tells Hibernate which variable we are using to 
	 * represent the parent class in our child class.
	 */
	@OneToMany(mappedBy="person", cascade = CascadeType.ALL)
	//@OneToMany(fetch = FetchType.EAGER, mappedBy="person", cascade = CascadeType.ALL)
	private List<Phone> phones;
	
	//@ManyToMany(mappedBy = "teamPersons", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	@ManyToMany(mappedBy = "teamPersons")
	List<Team> teams;

	


	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

}
