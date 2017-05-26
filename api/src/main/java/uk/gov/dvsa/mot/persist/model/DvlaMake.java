package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.util.Date ;
import java.util.List ;

/**
 * The persistent class for the dvla_make database table.
 * 
 */
public class DvlaMake implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String code ;
  private String name ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private int version ;
  private List<DvlaModel> dvlaModels ;

  public DvlaMake()
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

  public String getCode()
  {
    return this.code ;
  }

  public void setCode( String code )
  {
    this.code = code ;
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

  public List<DvlaModel> getDvlaModels()
  {
    return this.dvlaModels ;
  }

  public void setDvlaModels( List<DvlaModel> dvlaModels )
  {
    this.dvlaModels = dvlaModels ;
  }

  public DvlaModel addDvlaModel( DvlaModel dvlaModel )
  {
    getDvlaModels().add( dvlaModel ) ;
    dvlaModel.setDvlaMake( this ) ;

    return dvlaModel ;
  }

  public DvlaModel removeDvlaModel( DvlaModel dvlaModel )
  {
    getDvlaModels().remove( dvlaModel ) ;
    dvlaModel.setDvlaMake( null ) ;

    return dvlaModel ;
  }

}