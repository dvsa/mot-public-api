package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.math.BigDecimal ;
import java.util.Date ;
import java.util.List ;

/**
 * The persistent class for the enforcement_site_assessment database table.
 * 
 */
public class EnforcementSiteAssessment implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private int aeOrganisationId ;
  private String aeRepresentativeName ;
  private String aeRepresentativePosition ;
  private int createdBy ;
  private Date createdOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private BigDecimal siteAssessmentScore ;
  private int version ;
  private Date visitDate ;
  // @JoinColumn(name="ae_representative_person_id")
  private Person person1 ;
  // @JoinColumn(name="examiner_person_id", nullable=false)
  private Person person2 ;
  private Site site ;
  // @JoinColumn(name="tester_person_id")
  private Person person3 ;
  // @OneToMany(mappedBy="enforcementSiteAssessment")
  private List<Site> sites ;

  public EnforcementSiteAssessment()
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

  public int getAeOrganisationId()
  {
    return this.aeOrganisationId ;
  }

  public void setAeOrganisationId( int aeOrganisationId )
  {
    this.aeOrganisationId = aeOrganisationId ;
  }

  public String getAeRepresentativeName()
  {
    return this.aeRepresentativeName ;
  }

  public void setAeRepresentativeName( String aeRepresentativeName )
  {
    this.aeRepresentativeName = aeRepresentativeName ;
  }

  public String getAeRepresentativePosition()
  {
    return this.aeRepresentativePosition ;
  }

  public void setAeRepresentativePosition( String aeRepresentativePosition )
  {
    this.aeRepresentativePosition = aeRepresentativePosition ;
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

  public BigDecimal getSiteAssessmentScore()
  {
    return this.siteAssessmentScore ;
  }

  public void setSiteAssessmentScore( BigDecimal siteAssessmentScore )
  {
    this.siteAssessmentScore = siteAssessmentScore ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public Date getVisitDate()
  {
    return this.visitDate ;
  }

  public void setVisitDate( Date visitDate )
  {
    this.visitDate = visitDate ;
  }

  public Person getPerson1()
  {
    return this.person1 ;
  }

  public void setPerson1( Person person1 )
  {
    this.person1 = person1 ;
  }

  public Person getPerson2()
  {
    return this.person2 ;
  }

  public void setPerson2( Person person2 )
  {
    this.person2 = person2 ;
  }

  public Site getSite()
  {
    return this.site ;
  }

  public void setSite( Site site )
  {
    this.site = site ;
  }

  public Person getPerson3()
  {
    return this.person3 ;
  }

  public void setPerson3( Person person3 )
  {
    this.person3 = person3 ;
  }

  public List<Site> getSites()
  {
    return this.sites ;
  }

  public void setSites( List<Site> sites )
  {
    this.sites = sites ;
  }

  public Site addSite( Site site )
  {
    getSites().add( site ) ;
    site.setEnforcementSiteAssessment( this ) ;

    return site ;
  }

  public Site removeSite( Site site )
  {
    getSites().remove( site ) ;
    site.setEnforcementSiteAssessment( null ) ;

    return site ;
  }

}