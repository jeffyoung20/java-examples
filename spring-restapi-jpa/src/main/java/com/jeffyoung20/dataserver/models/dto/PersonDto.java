package com.jeffyoung20.dataserver.models.dto;

import java.util.List;


public class PersonDto {
	public long id;
	public String firstName;
	public String lastName;
    public List<PhoneDto> phones;
    
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
	public List<PhoneDto> getPhones() {
		return phones;
	}
	public void setPhones(List<PhoneDto> phones) {
		this.phones = phones;
	}
}
