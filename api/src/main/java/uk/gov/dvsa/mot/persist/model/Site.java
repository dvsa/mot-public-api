package uk.gov.dvsa.mot.persist.model ;

import java.io.Serializable ;
import java.math.BigDecimal ;
import java.util.Date ;

/**
 * The persistent class for the site database table.
 * 
 */
public class Site implements Serializable
{
  private static final long serialVersionUID = 1L ;

  private int id ;
  private int createdBy ;
  private Date createdOn ;
  private byte dualLanguage ;
  private int firstLiveTestCarriedOutNumber ;
  private Date firstLiveTestCarriedOutOn ;
  private Date firstLoginOn ;
  private int firstTestCarriedOutNumber ;
  private Date firstTestCarriedOutOn ;
  private int lastUpdatedBy ;
  private Date lastUpdatedOn ;
  private BigDecimal latitude ;
  private BigDecimal longitude ;
  private Date mot1DetailsUpdatedOn ;
  private String name ;
  private byte scottishBankHoliday ;
  private String siteNumber ;
  private Date statusChangedOn ;
  private int version ;
  private EnforcementSiteAssessment enforcementSiteAssessment ;
  // @JoinColumn(name="default_brake_test_class_1_and_2_id")
  private BrakeTestType brakeTestType1 ;
  // @JoinColumn(name="default_parking_brake_test_class_3_and_above_id")
  private BrakeTestType brakeTestType2 ;
  // @JoinColumn(name="default_service_brake_test_class_3_and_above_id")
  private BrakeTestType brakeTestType3 ;
  // @JoinColumn(name="first_live_test_carried_out_by")
  private Person person1 ;
  // @JoinColumn(name="first_login_by")
  private Person person2 ;
  // @JoinColumn(name="first_test_carried_out_by")
  private Person person3 ;
  private Mot1VtsDeviceStatus mot1VtsDeviceStatus ;
  private NonWorkingDayCountryLookup nonWorkingDayCountryLookup ;
  private Organisation organisation ;
  private SiteStatusLookup siteStatusLookup ;
  private TransitionStatus transitionStatus ;
  private SiteType siteType ;

  public Site()
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

  public byte getDualLanguage()
  {
    return this.dualLanguage ;
  }

  public void setDualLanguage( byte dualLanguage )
  {
    this.dualLanguage = dualLanguage ;
  }

  public int getFirstLiveTestCarriedOutNumber()
  {
    return this.firstLiveTestCarriedOutNumber ;
  }

  public void setFirstLiveTestCarriedOutNumber( int firstLiveTestCarriedOutNumber )
  {
    this.firstLiveTestCarriedOutNumber = firstLiveTestCarriedOutNumber ;
  }

  public Date getFirstLiveTestCarriedOutOn()
  {
    return this.firstLiveTestCarriedOutOn ;
  }

  public void setFirstLiveTestCarriedOutOn( Date firstLiveTestCarriedOutOn )
  {
    this.firstLiveTestCarriedOutOn = firstLiveTestCarriedOutOn ;
  }

  public Date getFirstLoginOn()
  {
    return this.firstLoginOn ;
  }

  public void setFirstLoginOn( Date firstLoginOn )
  {
    this.firstLoginOn = firstLoginOn ;
  }

  public int getFirstTestCarriedOutNumber()
  {
    return this.firstTestCarriedOutNumber ;
  }

  public void setFirstTestCarriedOutNumber( int firstTestCarriedOutNumber )
  {
    this.firstTestCarriedOutNumber = firstTestCarriedOutNumber ;
  }

  public Date getFirstTestCarriedOutOn()
  {
    return this.firstTestCarriedOutOn ;
  }

  public void setFirstTestCarriedOutOn( Date firstTestCarriedOutOn )
  {
    this.firstTestCarriedOutOn = firstTestCarriedOutOn ;
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

  public BigDecimal getLatitude()
  {
    return this.latitude ;
  }

  public void setLatitude( BigDecimal latitude )
  {
    this.latitude = latitude ;
  }

  public BigDecimal getLongitude()
  {
    return this.longitude ;
  }

  public void setLongitude( BigDecimal longitude )
  {
    this.longitude = longitude ;
  }

  public Date getMot1DetailsUpdatedOn()
  {
    return this.mot1DetailsUpdatedOn ;
  }

  public void setMot1DetailsUpdatedOn( Date mot1DetailsUpdatedOn )
  {
    this.mot1DetailsUpdatedOn = mot1DetailsUpdatedOn ;
  }

  public String getName()
  {
    return this.name ;
  }

  public void setName( String name )
  {
    this.name = name ;
  }

  public byte getScottishBankHoliday()
  {
    return this.scottishBankHoliday ;
  }

  public void setScottishBankHoliday( byte scottishBankHoliday )
  {
    this.scottishBankHoliday = scottishBankHoliday ;
  }

  public String getSiteNumber()
  {
    return this.siteNumber ;
  }

  public void setSiteNumber( String siteNumber )
  {
    this.siteNumber = siteNumber ;
  }

  public Date getStatusChangedOn()
  {
    return this.statusChangedOn ;
  }

  public void setStatusChangedOn( Date statusChangedOn )
  {
    this.statusChangedOn = statusChangedOn ;
  }

  public int getVersion()
  {
    return this.version ;
  }

  public void setVersion( int version )
  {
    this.version = version ;
  }

  public EnforcementSiteAssessment getEnforcementSiteAssessment()
  {
    return this.enforcementSiteAssessment ;
  }

  public void setEnforcementSiteAssessment( EnforcementSiteAssessment enforcementSiteAssessment )
  {
    this.enforcementSiteAssessment = enforcementSiteAssessment ;
  }

  public BrakeTestType getBrakeTestType1()
  {
    return this.brakeTestType1 ;
  }

  public void setBrakeTestType1( BrakeTestType brakeTestType1 )
  {
    this.brakeTestType1 = brakeTestType1 ;
  }

  public BrakeTestType getBrakeTestType2()
  {
    return this.brakeTestType2 ;
  }

  public void setBrakeTestType2( BrakeTestType brakeTestType2 )
  {
    this.brakeTestType2 = brakeTestType2 ;
  }

  public BrakeTestType getBrakeTestType3()
  {
    return this.brakeTestType3 ;
  }

  public void setBrakeTestType3( BrakeTestType brakeTestType3 )
  {
    this.brakeTestType3 = brakeTestType3 ;
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

  public Person getPerson3()
  {
    return this.person3 ;
  }

  public void setPerson3( Person person3 )
  {
    this.person3 = person3 ;
  }

  public Mot1VtsDeviceStatus getMot1VtsDeviceStatus()
  {
    return this.mot1VtsDeviceStatus ;
  }

  public void setMot1VtsDeviceStatus( Mot1VtsDeviceStatus mot1VtsDeviceStatus )
  {
    this.mot1VtsDeviceStatus = mot1VtsDeviceStatus ;
  }

  public NonWorkingDayCountryLookup getNonWorkingDayCountryLookup()
  {
    return this.nonWorkingDayCountryLookup ;
  }

  public void setNonWorkingDayCountryLookup( NonWorkingDayCountryLookup nonWorkingDayCountryLookup )
  {
    this.nonWorkingDayCountryLookup = nonWorkingDayCountryLookup ;
  }

  public Organisation getOrganisation()
  {
    return this.organisation ;
  }

  public void setOrganisation( Organisation organisation )
  {
    this.organisation = organisation ;
  }

  public SiteStatusLookup getSiteStatusLookup()
  {
    return this.siteStatusLookup ;
  }

  public void setSiteStatusLookup( SiteStatusLookup siteStatusLookup )
  {
    this.siteStatusLookup = siteStatusLookup ;
  }

  public TransitionStatus getTransitionStatus()
  {
    return this.transitionStatus ;
  }

  public void setTransitionStatus( TransitionStatus transitionStatus )
  {
    this.transitionStatus = transitionStatus ;
  }

  public SiteType getSiteType()
  {
    return this.siteType ;
  }

  public void setSiteType( SiteType siteType )
  {
    this.siteType = siteType ;
  }
}