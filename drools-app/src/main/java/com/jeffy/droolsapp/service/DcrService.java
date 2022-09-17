package com.jeffy.droolsapp.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeffy.droolsapp.models.Dcr;
import com.jeffy.droolsapp.models.Entity;
import com.jeffy.droolsapp.models.RoutingInfo;

@Service
public class DcrService {
	@Autowired
    private KieContainer kieContainer;

    public RoutingInfo routeDcr(Dcr dcr) {
    	RoutingInfo routingInfo = new RoutingInfo();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(dcr);
        kieSession.insert(routingInfo);
        kieSession.fireAllRules();
        kieSession.dispose();
        return routingInfo; 
    }
}
