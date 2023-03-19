package com.jeffyoung20.poormansjpa.repos;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.jeffyoung20.poormansjpa.library.CrudRepository;
import com.jeffyoung20.poormansjpa.models.ModelPerson;

@Component
public class RepoPerson extends CrudRepository<ModelPerson, Long> {

	public RepoPerson(JdbcTemplate jdbcTemplate) {
		super(ModelPerson.class, jdbcTemplate);
	}

}
