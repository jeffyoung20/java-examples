package com.jeffyoung20.dataserver.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeffyoung20.dataserver.models.data.Person;


public interface RepoPerson extends JpaRepository<Person, Long> {
	public List<Person> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
