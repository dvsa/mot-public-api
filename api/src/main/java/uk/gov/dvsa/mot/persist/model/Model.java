package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.sql.Timestamp ;

/**
 * The persistent class for the model database table.
 * 
 */
public class Model implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private Make make ;
  private String code ;
  private String name ;
  private boolean isVerified ;
  private int createdBy ;
  private Timestamp createdOn ;
  private int lastUpdatedBy ;
  private Timestamp lastUpdatedOn ;
  private int version ;
  // private List<ModelDetail> modelDetails;

  public Model()
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

  public Make getMake()
  {
    return this.make ;
  }

  public void setMake( Make make )
  {
    this.make = make ;
  }

  /*
   * public List<ModelDetail> getModelDetails() { return this.modelDetails; }
   * 
   * public void setModelDetails( List<ModelDetail> modelDetails ) {
   * this.modelDetails = modelDetails; }
   * 
   * public ModelDetail addModelDetail( ModelDetail modelDetail ) {
   * getModelDetails().add( modelDetail ); modelDetail.setModel( this );
   * 
   * return modelDetail; }
   * 
   * public ModelDetail removeModelDetail( ModelDetail modelDetail ) {
   * getModelDetails().remove( modelDetail ); modelDetail.setModel( null );
   * 
   * return modelDetail; }
   */
}