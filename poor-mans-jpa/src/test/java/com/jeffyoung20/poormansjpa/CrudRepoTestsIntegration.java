package com.jeffyoung20.poormansjpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jeffyoung20.poormansjpa.models.ModelPerson;

@SpringBootTest
class CrudRepoTestsIntegration {
	private Logger LOGGER = LoggerFactory.getLogger(CrudRepoTests.class);
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
	@Test
	public void testSelect() throws Exception {
		long personId = 1;
		RepoPerson repoPerson = new RepoPerson(jdbcTemplate);
		ModelPerson person = repoPerson.findById(personId);
		
		LOGGER.info(person.toString());
	}
	
	@Test
	public void testSave() throws Exception {
		RepoPerson repoPerson = new RepoPerson(jdbcTemplate);
		ModelPerson person = new ModelPerson();

		long id=2;
		person.setId(id);
		person.setFirstName("George");
		person.setLastName("Washington");
		repoPerson.save(person);
		
		person = repoPerson.findById(id);
		
		LOGGER.info(person.toString());
	}
}
