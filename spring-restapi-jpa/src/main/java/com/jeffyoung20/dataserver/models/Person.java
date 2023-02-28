package com.jeffyoung20.dataserver.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
//@RequiredArgsConstructor
//@Table(name = "person")
public class Person {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	public String firstName;
	public String lastName;
	
	/*
	 * NOTE:  mappedBy property tells Hibernate which variable we are using to 
	 * represent the parent class in our child class.
	 */
	@OneToMany(mappedBy="person", cascade = CascadeType.ALL)
	//@OneToMany(fetch = FetchType.EAGER, mappedBy="person", cascade = CascadeType.ALL)
    public List<Address> addresses;
}
