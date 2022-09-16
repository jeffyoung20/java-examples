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
		RoutingInfo ri = new RoutingInfo();
		ri.routeTo = "Unknown";
	    Dcr dcr = new Dcr();
	    Entity entity = new Entity();
	    entity.country = "US";
	    dcr.entity = entity;
	    String destination = service.routeDcr(dcr, ri);
	    logger.info("RouteTo: {}",ri.routeTo);
	    Assertions.assertEquals("internal",ri.routeTo);
	}

	@Test
	public void testRule_NonUSToExternal() throws Exception {
		RoutingInfo ri = new RoutingInfo();
		ri.routeTo = "Unknown";
	    Dcr dcr = new Dcr();
	    Entity entity = new Entity();
	    entity.country = "ES";
	    dcr.entity = entity;
	    String destination = service.routeDcr(dcr, ri);
	    logger.info("RouteTo: {}",ri.routeTo);
	    Assertions.assertEquals("external",ri.routeTo);
	}
}
