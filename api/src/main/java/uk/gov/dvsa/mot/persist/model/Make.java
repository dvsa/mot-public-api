package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the make database table.
 * 
 */
public class Make implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private String code ;
  private String name ;
  private boolean isVerified ;
  private boolean isSelectable ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;
  // private List<Model> models;

  public Make()
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

  public Timestamp getCreatedOn()
  {
    return this.createdOn ;
  }

  public void setCreatedOn( Timestamp createdOn )
  {
    this.createdOn = createdOn ;
  }

  public boolean getIsVerified()
  {
    return this.isVerified ;
  }

  public void setIsVerified( boolean isVerified )
  {
    this.isVerified = isVerified ;
  }

  public boolean getIsSelectable()
  {
    return this.isSelectable ;
  }

  public void setIsSelectable( boolean isSelectable )
  {
    this.isSelectable = isSelectable ;
  }

  public int getLastUpdatedBy()
  {
    return this.lastUpdatedBy ;
  }

  public void setLastUpdatedBy( int lastUpdatedBy )
  {
    this.lastUpdatedBy = lastUpdatedBy ;
  }

  public Timestamp getLastUpdatedOn()
  {
    return this.lastUpdatedOn ;
  }

  public void setLastUpdatedOn( Timestamp lastUpdatedOn )
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

  /*
   * public List<Model> getModels() { return this.models; }
   * 
   * public void setModels( List<Model> models ) { this.models = models; }
   * 
   * public Model addModel( Model model ) { getModels().add( model );
   * model.setMake( this );
   * 
   * return model; }
   * 
   * public Model removeModel( Model model ) { getModels().remove( model );
   * model.setMake( null );
   * 
   * return model; }
   */

}