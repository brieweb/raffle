package com.raffleitup.domain;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import java.lang.Override;

import com.raffleitup.domain.Contestant;

import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Winner implements Serializable
{

   @Id
   private @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   Long id = null;
   @Version
   private @Column(name = "version")
   int version = 0;

   @ManyToOne
   private Contestant contestant;

   @OneToOne
   private Prize myPrize;

   private @Temporal(TemporalType.TIMESTAMP)
   Date winTime;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Winner) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public Contestant getContestant()
   {
      return this.contestant;
   }

   public void setContestant(final Contestant contestant)
   {
      this.contestant = contestant;
   }

   public Prize getMyPrize()
   {
      return myPrize;
   }

   public void setMyPrize(Prize myPrize)
   {
      this.myPrize = myPrize;
   }

   public Date getWinTime()
   {
      return this.winTime;
   }

   public void setWinTime(final Date winTime)
   {
      this.winTime = winTime;
   }
}