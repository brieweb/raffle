package com.raffleitup.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.cdi.push.Push;

import com.raffleitup.domain.Contestant;
import com.raffleitup.service.ContestantRegistration;


@Model
public class ContestantController {
	public static final String PUSH_CDI_TOPIC = "pushCdi2";

	   @Inject
	   private FacesContext facesContext;
	   
	   @Inject
	   private ContestantRegistration contestantRegistration;

	   @Inject
	   @Push(topic = PUSH_CDI_TOPIC) Event<String> pushEvent;
	   
	   private Contestant newContestant;
	   private Contestant contestant;
	   
	   @Produces
	   @Named
	   public Contestant getNewContestant() {
		   return newContestant;
	   }
	   
	   @Produces 
	   @Named
	   public Contestant getContestant() {
		   return contestant;
	   }

	public void setContestant(Contestant contestant) {
		this.contestant = contestant;
	}
	
	   public void register() throws Exception {
		      contestantRegistration.register(newContestant);    
		      facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
		      pushEvent.fire(String.format("New contestant added: %s (id: %d)", newContestant.getFirstName(), newContestant.getId()));
		      initNewContestant();
		   }
	   
	   @PostConstruct
	   public void initNewContestant() {
	      newContestant = new Contestant();
	   }

}
