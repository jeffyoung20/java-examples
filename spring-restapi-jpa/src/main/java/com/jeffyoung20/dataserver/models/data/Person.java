package com.jeffyoung20.dataserver.models.data;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Data
@Entity
//@RequiredArgsConstructor
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
	private List<Address> addresses;

	
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

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
}
