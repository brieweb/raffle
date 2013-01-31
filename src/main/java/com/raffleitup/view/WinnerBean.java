package com.raffleitup.view;

import java.beans.Expression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

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
import javax.sound.sampled.ReverbType;
import javax.swing.event.ListSelectionEvent;

import com.raffleitup.domain.Contestant;
import com.raffleitup.domain.Winner;

/**
 * Backing bean for Winner entities.
 * <p>
 * This class provides CRUD functionality for all Winner entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class WinnerBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Winner entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private Winner winner;

   public Winner getWinner()
   {
      return this.winner;
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
         this.winner = this.example;
      }
      else
      {
         this.winner = findById(getId());
      }
   }

   public Winner findById(Long id)
   {

      return this.entityManager.find(Winner.class, id);
   }

   /*
    * Support updating and deleting Winner entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.winner);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.winner);
            return "view?faces-redirect=true&id=" + this.winner.getId();
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
    * Support searching Winner entities with pagination
    */

   private int page;
   private long count;
   private List<Winner> pageItems;

   private Winner example = new Winner();

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

   public Winner getExample()
   {
      return this.example;
   }

   public void setExample(Winner example)
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
      Root<Winner> root = countCriteria.from(Winner.class);
      countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Winner> criteria = builder.createQuery(Winner.class);
      root = criteria.from(Winner.class);
      //Expression<Number> fetchNum;
      criteria.orderBy(builder.desc(root.get("id")));
      TypedQuery<Winner> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
      
      query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Winner> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Contestant contestant = this.example.getContestant();
      if (contestant != null)
      {
         predicatesList.add(builder.equal(root.get("contestant"), contestant));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Winner> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Winner entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Winner> getAll()
   {

	   List<Winner> result;
      //CriteriaQuery<Winner> criteria = this.entityManager.getCriteriaBuilder().createQuery(Winner.class);
	  // CriteriaQuery<Winner> criteria = this.entityManager.getCriteriaBuilder().
      //result = (this.entityManager.createQuery(   criteria.select( criteria.from(Winner.class)  )  ) .getResultList() );
      //return Collections.reverse(result);
	   result = this.entityManager.createNamedQuery("from Winner order by id asc").getResultList();
      return   result;
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final WinnerBean ejbProxy = this.sessionContext.getBusinessObject(WinnerBean.class);

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

            return String.valueOf(((Winner) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Winner add = new Winner();

   public Winner getAdd()
   {
      return this.add;
   }

   public Winner getAdded()
   {
      Winner added = this.add;
      this.add = new Winner();
      return added;
   }
   

}