package com.raffleitup.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.raffleitup.domain.Contestant_;
import com.raffleitup.domain.Prize_;
import com.raffleitup.domain.Contestant;
import com.raffleitup.domain.Prize;
import com.raffleitup.domain.Winner;

@Named
@Stateful
@ConversationScoped
public class PickWinnerBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Random generator = new Random();
	
	private Contestant contestant;
	
	private Prize prize;
	
	   @Inject
	   private Conversation conversation;

	   @PersistenceContext(type = PersistenceContextType.EXTENDED)
	   private EntityManager entityManager;
	   
	   public List<Contestant> getNonWinContestant() {
		   CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		   CriteriaQuery<Contestant> cq = cb.createQuery(Contestant.class);
		   //Metamodel m = entityManager.getMetamodel();
		   //EntityType<Prize> Prize_ = m.entity(Prize.class);
		   Root<Contestant> prizert = cq.from(Contestant.class);
		   //cq.where(cb.equal(prizert.get(prize), false))
		   cq.where(cb.equal(  prizert.get(Contestant_.wonPrize), false) );
		   
		   TypedQuery<Contestant> q = entityManager.createQuery(cq);
		   List<Contestant> results = q.getResultList();
		   return results;
	   }

	   public List<Contestant> getAllContestant()
	   {

	      CriteriaQuery<Contestant> criteria = this.entityManager.getCriteriaBuilder().createQuery(Contestant.class);
	      return this.entityManager.createQuery(criteria.select(criteria.from(Contestant.class))).getResultList();
	   }
	   
	   public String winner() {
		   List<Contestant> myC = getNonWinContestant();
		   //int sz = myC.size();
		   
		   int randomIndex = generator.nextInt( myC.size());
		   
//		   int winner =  randomIndex % sz   ;
//		   if (winner < 0 ) 
//			   winner += sz;
		   
		   entityManager.merge(prize);
		  
		   System.out.println("Id is " + randomIndex);
		   this.contestant = myC.get(randomIndex);
		   this.contestant.setWonPrize(true);
		   //this.id = contestant.getId();
		   Winner w = new Winner();
		   w.setContestant(contestant);
		   prize.setWon(true);
		   w.setMyPrize(prize);
		   Date now = new Date();
		   w.setWinTime(now);
		   entityManager.persist(w);
		   //this.contestant = findById(this.id);
		   //myC.get(index)
		   //retrieve();
		   return "";

	   }
	   
	   public String randContestant() {
		   conversation.begin();
		   List<Contestant> myC = getNonWinContestant();
		   //int sz = myC.size();
		   
		   int randomIndex = generator.nextInt( myC.size());
		   this.contestant = myC.get(randomIndex);
		   //this.contestant.setWonPrize(true);
		   return "getprize";
	   }
	   
	   public String selectPrize() {
		   //this.id = contestant.getId();
		   Winner w = new Winner();
		   w.setContestant(contestant);
		   contestant.setWonPrize(true);
		   prize.setWon(true);
		   w.setMyPrize(prize);
		   Date now = new Date();
		   w.setWinTime(now);
		   entityManager.persist(w);
		   //entityManager.persist(contestant);
		   conversation.end();
		   return "/admin/winner/search?faces-redirect=true";
		   
	   }
	   
	   public Contestant getContestant()
	   {
	      return this.contestant;
	   }
	   
	   public Prize findPrizeById(Long id)
	   {

	      return this.entityManager.find(Prize.class, id);
	   }
	   
	   @Resource
	   private SessionContext sessionContext;

	   public Converter getPrizeConverter()
	   {

	      final PickWinnerBean ejbProxy = this.sessionContext.getBusinessObject(PickWinnerBean.class);

	      return new Converter()
	      {

	         @Override
	         public Object getAsObject(FacesContext context, UIComponent component, String value)
	         {

	            return ejbProxy.findPrizeById(Long.valueOf(value));
	         }

	         @Override
	         public String getAsString(FacesContext context, UIComponent component, Object value)
	         {

	            if (value == null)
	            {
	               return "";
	            }

	            return String.valueOf(((Prize) value).getId());
	         }
	      };
	   }
	   
	   public List<Prize> getAllPrize()
	   {

//	      CriteriaQuery<Prize> criteria = this.entityManager.getCriteriaBuilder().createQuery(Prize.class);
//	      return this.entityManager.createQuery(criteria.select(criteria.from(Prize.class))).getResultList();
	      
//		   CriteriaQuery<Prize> criteria = this.entityManager.getCriteriaBuilder().createQuery(Prize.class); 
//		   Metamodel m = entityManager.getMetamodel();
//		   EntityType<Prize> Prize_ = m.entity(Prize.class);
//		   Root<Prize> prize = criteria.from(Prize.class);
//		   criteria.where(prize.get(Prize_.).eq(false));
//		   return this.entityManager.createQuery(criteria.select(criteria.from(Prize.class))).getResultList();
		   
		   //CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		   CriteriaQuery<Prize> cq = cb.createQuery(Prize.class);
//		   //CriteriaQuery<Prize> cq = cb.createQuery(Prize.class);
//		   Metamodel m = entityManager.getMetamodel();
//		   EntityType<Prize> Prize_ = m.entity(Prize.class);
//		   Root<Prize> pet = cq.from(Prize.class);
//		   cq.where(cb.equal(pet.get(Prize_.), false));
		   
		   CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		   CriteriaQuery<Prize> cq = cb.createQuery(Prize.class);
		   //Metamodel m = entityManager.getMetamodel();
		   //EntityType<Prize> Prize_ = m.entity(Prize.class);
		   Root<Prize> prizert = cq.from(Prize.class);
		   //cq.where(cb.equal(prizert.get(prize), false))
		   cq.where(cb.equal(  prizert.get(Prize_.won), false) );
		   
		   TypedQuery<Prize> q = entityManager.createQuery(cq);
		   List<Prize> results = q.getResultList();
		   return results;
		   
	   }

	public Prize getPrize() {
		return prize;
	}

	public void setPrize(Prize prize) {
		this.prize = prize;
	}
	
}
