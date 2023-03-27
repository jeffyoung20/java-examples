package com.jeffyoung20.testapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//@Data
//@Entity
@Table(name = "person", schema = "test") 
public class ModelPerson {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	protected long id;
	
	@Column(name="first_name") 
	protected String firstName;
	
	@Column(name="last_name")
	protected String lastName;

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
}
