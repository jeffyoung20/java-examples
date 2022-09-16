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

    public String routeDcr(Dcr dcr, RoutingInfo routingInfo) {
        KieSession kieSession = kieContainer.newKieSession();
        //kieSession.setGlobal("dcr", dcr);
        kieSession.insert(dcr);
        kieSession.insert(routingInfo);
        kieSession.fireAllRules();
        kieSession.dispose();
        return dcr.destination; 
    }
}
