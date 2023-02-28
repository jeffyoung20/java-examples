package com.jeffyoung20.dataserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
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

}
