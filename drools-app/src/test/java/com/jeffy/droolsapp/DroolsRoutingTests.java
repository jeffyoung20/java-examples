package com.jeffy.droolsapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import com.jeffy.droolsapp.models.Dcr;
import com.jeffy.droolsapp.models.Entity;
import com.jeffy.droolsapp.models.RoutingInfo;
import com.jeffy.droolsapp.service.DcrRulesConfig;
import com.jeffy.droolsapp.service.DcrService;

@SpringBootTest
class DroolsRoutingTests {
	private static Logger logger = LoggerFactory.getLogger(DroolsAppRunner.class);
	
	@Autowired
	private DcrService service;
	
	@Test
	public void testRule_USToInternal() throws Exception {
		//Arrange
	    Entity entity = new Entity();
	    entity.country = "US";
	    Dcr dcr = new Dcr();
	    dcr.entity = entity;

	    //Act
	    RoutingInfo ri = service.routeDcr(dcr);
	    logger.info("RouteTo: {}",ri.routeTo);
	    
	    //Assert
	    Assertions.assertEquals("internal",ri.routeTo);
	    Assertions.assertEquals("internal",dcr.destination);
	}

	@Test
	public void testRule_NonUSToExternal() throws Exception {
		//Arrange
	    Entity entity = new Entity();
	    entity.country = "ES";
	    Dcr dcr = new Dcr();
	    dcr.entity = entity;

	    //Act
	    RoutingInfo ri = service.routeDcr(dcr);
	    logger.info("RouteTo: {}",ri.routeTo);
	    
	    //Assert
	    Assertions.assertEquals("external",ri.routeTo);
	    Assertions.assertEquals("external",dcr.destination);
	}
}
