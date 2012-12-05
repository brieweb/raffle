package com.raffleitup.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.raffleitup.domain.Contestant;
import com.raffleitup.domain.Prize;
import com.raffleitup.domain.Winner;

/**
 * Backing bean for Contestant entities.
 * <p>
 * This class provides CRUD functionality for all Contestant entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ContestantBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Contestant entities
    */

   private Long id;
   
   private Random generator = new Random();

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private Contestant contestant;

   public Contestant getContestant()
   {
      return this.contestant;
   }
   

   
   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.contestant = this.example;
      }
      else
      {
         this.contestant = findById(getId());
      }
   }
   
   public void winner() {
	   List<Contestant> myC = getAll();
	   int sz = myC.size();
	   
	   int randomIndex = generator.nextInt( );
	   
	   int winner =  randomIndex % sz   ;
	   if (winner < 0 ) 
		   winner += sz;
	  
	   System.out.println("Id is " + winner);
	   this.contestant = myC.get(winner);
	   this.id = contestant.getId();
	   Winner w = new Winner();
	   w.setContestant(contestant);
	   entityManager.persist(w);
	   //this.contestant = findById(this.id);
	   //myC.get(index)
	   //retrieve();

   }

   public Contestant findById(Long id)
   {

      return this.entityManager.find(Contestant.class, id);
   }

   /*
    * Support updating and deleting Contestant entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.contestant);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.contestant);
            return "view?faces-redirect=true&id=" + this.contestant.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         this.entityManager.remove(findById(getId()));
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Contestant entities with pagination
    */

   private int page;
   private long count;
   private List<Contestant> pageItems;
   
   

   private Contestant example = new Contestant();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Contestant getExample()
   {
      return this.example;
   }

   public void setExample(Contestant example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Contestant> root = countCriteria.from(Contestant.class);
      countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Contestant> criteria = builder.createQuery(Contestant.class);
      root = criteria.from(Contestant.class);
      TypedQuery<Contestant> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Contestant> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String firstName = this.example.getFirstName();
      if (firstName != null && !"".equals(firstName))
      {
         predicatesList.add(builder.like(root.<String> get("firstName"), '%' + firstName + '%'));
      }
      String lastName = this.example.getLastName();
      if (lastName != null && !"".equals(lastName))
      {
         predicatesList.add(builder.like(root.<String> get("lastName"), '%' + lastName + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Contestant> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Contestant entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Contestant> getAll()
   {

      CriteriaQuery<Contestant> criteria = this.entityManager.getCriteriaBuilder().createQuery(Contestant.class);
      return this.entityManager.createQuery(criteria.select(criteria.from(Contestant.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ContestantBean ejbProxy = this.sessionContext.getBusinessObject(ContestantBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context, UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context, UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Contestant) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Contestant add = new Contestant();

   public Contestant getAdd()
   {
      return this.add;
   }

   public Contestant getAdded()
   {
      Contestant added = this.add;
      this.add = new Contestant();
      return added;
   }






}