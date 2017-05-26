package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;

/**
 * The persistent class for the comment database table.
 * 
 */
public class Comment implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private long id ;
  private Person person ;
  private String comment ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;

  public Comment()
  {
  }

  public long getId()
  {
    return this.id ;
  }

  public void setId( long id )
  {
    this.id = id ;
  }

  public String getComment()
  {
    return this.comment ;
  }

  public void setComment( String comment )
  {
    this.comment = comment ;
  }

  public int getCreatedBy()
  {
    return this.createdBy ;
  }

  public void setCreatedBy( int createdBy )
  {
    this.createdBy = createdBy ;
  }

  public Date getCreatedOn()
  {
    return this.createdOn ;
  }

  public void setCreatedOn( Date createdOn )
  {
    this.createdOn = createdOn ;
  }

  public int getLastUpdatedBy()
  {
    return this.lastUpdatedBy ;
  }

  public void setLastUpdatedBy( int lastUpdatedBy )
  {
    this.lastUpdatedBy = lastUpdatedBy ;
  }

  public Date getLastUpdatedOn()
  {
    return this.lastUpdatedOn ;
  }

  public void setLastUpdatedOn( Date lastUpdatedOn )
  {
    this.lastUpdatedOn = lastUpdatedOn ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public Person getPerson()
  {
    return this.person ;
  }

  public void setPerson( Person person )
  {
    this.person = person ;
  }

}