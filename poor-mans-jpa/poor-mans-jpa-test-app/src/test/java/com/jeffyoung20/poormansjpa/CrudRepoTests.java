package com.jeffyoung20.poormansjpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeffyoung20.testapp.repos.RepoPerson;

public class CrudRepoTests {
	private Logger LOGGER = LoggerFactory.getLogger(CrudRepoTests.class);
	
	
	@Test
	public void testGenerateSelect() throws Exception {
		long personId = 5;
		RepoPerson repoPerson = new RepoPerson(null);
		String sql = repoPerson.genSqlSelectById(personId);
		
		LOGGER.info(sql);
	}
}
