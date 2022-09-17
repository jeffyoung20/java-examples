package com.jeffy.droolsapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jeffy.droolsapp.models.Dcr;
import com.jeffy.droolsapp.models.Entity;
import com.jeffy.droolsapp.models.RoutingInfo;
import com.jeffy.droolsapp.service.DcrService;


//@Component
public class DroolsAppRunner implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(DroolsAppRunner.class);
	
	@Autowired
	private DcrService service;
	
	@Override
	public void run(String... args) throws Exception {
	    Dcr dcr = getDcr("US");
	    RoutingInfo ri = service.routeDcr(dcr);
	    logger.info("RouteTo: {}",ri.routeTo);

		dcr = getDcr("CN");
		ri =service.routeDcr(dcr);
	    logger.info("RouteTo: {}",ri.routeTo);
}

	private Dcr getDcr(String country) {
		Dcr dcr = new Dcr();
	    //dcr.uri = "changeRequest/1";
	    Entity entity = new Entity();
	    //entity.uri = "entities/1";
	    //entity.country = "US";
	    entity.country = country;
	    dcr.entity = entity;
		return dcr;
	}

}
