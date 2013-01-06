package com.raffleitup.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.raffleitup.domain.Contestant;



@Stateless
public class ContestantRegistration {
	   @Inject
	   private Logger log;

	   @Inject
	   private EntityManager em;

	   @Inject
	   private Event<Contestant> contestantEventSrc;
	   
	   public void register(Contestant contestant) throws Exception {
		      log.info("Registering " + contestant.getFirstName());
		      em.persist(contestant);
		      contestantEventSrc.fire(contestant);
		   }
}
