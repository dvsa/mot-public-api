package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;

/**
 * The persistent class for the business_rule database table.
 * 
 */
public class BusinessRule implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String name ;
  private String definition ;
  private String comparison ;
  private Date dateValue ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;
  private BusinessRuleType businessRuleType ;

  public BusinessRule()
  {
  }

  public int getId()
  {
    return this.id ;
  }

  public void setId( int id )
  {
    this.id = id ;
  }

  public String getComparison()
  {
    return this.comparison ;
  }

  public void setComparison( String comparison )
  {
    this.comparison = comparison ;
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

  public Date getDateValue()
  {
    return this.dateValue ;
  }

  public void setDateValue( Date dateValue )
  {
    this.dateValue = dateValue ;
  }

  public String getDefinition()
  {
    return this.definition ;
  }

  public void setDefinition( String definition )
  {
    this.definition = definition ;
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

  public String getName()
  {
    return this.name ;
  }

  public void setName( String name )
  {
    this.name = name ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public BusinessRuleType getBusinessRuleType()
  {
    return this.businessRuleType ;
  }

  public void setBusinessRuleType( BusinessRuleType businessRuleType )
  {
    this.businessRuleType = businessRuleType ;
  }
}