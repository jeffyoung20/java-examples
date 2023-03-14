package com.jeffyoung20.dataserver.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeffyoung20.dataserver.models.data.Person;


public interface PersonRepo extends JpaRepository<Person, Long> {
	public List<Person> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
