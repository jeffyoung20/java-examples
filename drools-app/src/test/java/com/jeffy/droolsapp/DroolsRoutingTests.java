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
@ContextConfiguration(classes = DcrRulesConfig.class)
class DroolsRoutingTests {
	private static Logger logger = LoggerFactory.getLogger(DroolsAppRunner.class);
	
	@Autowired
	private DcrService service;
	
	@Test
	public void testRule1() throws Exception {
		RoutingInfo ri = new RoutingInfo();
		ri.routeTo = "Unknown";
	    Dcr dcr = new Dcr();
//	    dcr.uri = "changeRequest/1";
	    Entity entity = new Entity();
//	    entity.uri = "entities/1";
	    entity.country = "US";
	    dcr.entity = entity;
	    String destination = service.routeDcr(dcr, ri);
	    logger.info("RouteTo: {}",ri.routeTo);
	    Assertions.assertEquals("internal",ri.routeTo);
	}

}
