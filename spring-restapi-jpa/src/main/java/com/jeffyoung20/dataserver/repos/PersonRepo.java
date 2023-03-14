package com.jeffyoung20.dataserver.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jeffyoung20.dataserver.models.data.Person;
import com.jeffyoung20.dataserver.models.data.Phone;

import jakarta.persistence.Entity;
import lombok.Data;


public interface PersonRepo extends JpaRepository<Person, Long> {
	public List<Person> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
