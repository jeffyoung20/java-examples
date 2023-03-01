package com.jeffyoung20.dataserver.models.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

//@Data
@Entity
public class Address {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	public String line1;
	public String line2;
	public String city;
	public String state;
	public int	zip;
	
	/*
	 * NOTES: 
	 * - "person" field name matches @OneToMany(mappedBy="person") in Person Class
	 * - JsonIgnore to avoid circular references
	 */
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="person_id")
    public Person person;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
