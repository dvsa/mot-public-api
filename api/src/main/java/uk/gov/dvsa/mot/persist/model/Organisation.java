package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the organisation database table.
 */
public class Organisation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int createdBy;
    private Date createdOn;
    private byte dataMayBeDisclosed;
    private Date firstPaymentSetupOn;
    private Date firstSlotsPurchasedOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private Date mot1DetailsUpdatedOn;
    private Date mot1SlotsMigratedOn;
    private BigDecimal mot1TotalRemainderBalance;
    private BigDecimal mot1TotalRunningBalance;
    private int mot1TotalSlotsConverted;
    private int mot1TotalSlotsMerged;
    private int mot1TotalVtsSlotsMerged;
    private String name;
    private String registeredCompanyNumber;
    private Date sitesConfirmedReadyOn;
    private int slotsBalance;
    private int slotsOverdraft;
    private int slotsPurchased;
    private int slotsWarning;
    private String tradingName;
    private Date transitionProcessedOn;
    private Date transitionScheduledOn;
    private String vatRegistrationNumber;
    private int version;
    // private List<AuthForAe> authForAes;
    // private List<DirectDebit> directDebits;
    // private List<EventOrganisationMap> eventOrganisationMaps;
    private CompanyType companyType;
    private OrganisationType organisationType;
    // private TransitionStatus transitionStatus;
    // private List<OrganisationAssemblyRoleMap> organisationAssemblyRoleMaps;
    // private List<OrganisationBusinessRoleMap> organisationBusinessRoleMaps;
    // private List<OrganisationContactDetailMap> organisationContactDetailMaps;
    // private List<OrganisationSiteMap> organisationSiteMaps;
    private List<Site> sites;
    // private List<TestSlotTransaction> testSlotTransactions;
    // private List<TestSlotTransactionAmendment> testSlotTransactionAmendments;

    public Organisation() {

    }

    public int getId() {

        return this.id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getCreatedBy() {

        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {

        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {

        return this.createdOn;
    }

    public void setCreatedOn(Date createdOn) {

        this.createdOn = createdOn;
    }

    public byte getDataMayBeDisclosed() {

        return this.dataMayBeDisclosed;
    }

    public void setDataMayBeDisclosed(byte dataMayBeDisclosed) {

        this.dataMayBeDisclosed = dataMayBeDisclosed;
    }

    public Date getFirstPaymentSetupOn() {

        return this.firstPaymentSetupOn;
    }

    public void setFirstPaymentSetupOn(Date firstPaymentSetupOn) {

        this.firstPaymentSetupOn = firstPaymentSetupOn;
    }

    public Date getFirstSlotsPurchasedOn() {

        return this.firstSlotsPurchasedOn;
    }

    public void setFirstSlotsPurchasedOn(Date firstSlotsPurchasedOn) {

        this.firstSlotsPurchasedOn = firstSlotsPurchasedOn;
    }

    public int getLastUpdatedBy() {

        return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {

        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedOn() {

        return this.lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {

        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Date getMot1DetailsUpdatedOn() {

        return this.mot1DetailsUpdatedOn;
    }

    public void setMot1DetailsUpdatedOn(Date mot1DetailsUpdatedOn) {

        this.mot1DetailsUpdatedOn = mot1DetailsUpdatedOn;
    }

    public Date getMot1SlotsMigratedOn() {

        return this.mot1SlotsMigratedOn;
    }

    public void setMot1SlotsMigratedOn(Date mot1SlotsMigratedOn) {

        this.mot1SlotsMigratedOn = mot1SlotsMigratedOn;
    }

    public BigDecimal getMot1TotalRemainderBalance() {

        return this.mot1TotalRemainderBalance;
    }

    public void setMot1TotalRemainderBalance(BigDecimal mot1TotalRemainderBalance) {

        this.mot1TotalRemainderBalance = mot1TotalRemainderBalance;
    }

    public BigDecimal getMot1TotalRunningBalance() {

        return this.mot1TotalRunningBalance;
    }

    public void setMot1TotalRunningBalance(BigDecimal mot1TotalRunningBalance) {

        this.mot1TotalRunningBalance = mot1TotalRunningBalance;
    }

    public int getMot1TotalSlotsConverted() {

        return this.mot1TotalSlotsConverted;
    }

    public void setMot1TotalSlotsConverted(int mot1TotalSlotsConverted) {

        this.mot1TotalSlotsConverted = mot1TotalSlotsConverted;
    }

    public int getMot1TotalSlotsMerged() {

        return this.mot1TotalSlotsMerged;
    }

    public void setMot1TotalSlotsMerged(int mot1TotalSlotsMerged) {

        this.mot1TotalSlotsMerged = mot1TotalSlotsMerged;
    }

    public int getMot1TotalVtsSlotsMerged() {

        return this.mot1TotalVtsSlotsMerged;
    }

    public void setMot1TotalVtsSlotsMerged(int mot1TotalVtsSlotsMerged) {

        this.mot1TotalVtsSlotsMerged = mot1TotalVtsSlotsMerged;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getRegisteredCompanyNumber() {

        return this.registeredCompanyNumber;
    }

    public void setRegisteredCompanyNumber(String registeredCompanyNumber) {

        this.registeredCompanyNumber = registeredCompanyNumber;
    }

    public Date getSitesConfirmedReadyOn() {

        return this.sitesConfirmedReadyOn;
    }

    public void setSitesConfirmedReadyOn(Date sitesConfirmedReadyOn) {

        this.sitesConfirmedReadyOn = sitesConfirmedReadyOn;
    }

    public int getSlotsBalance() {

        return this.slotsBalance;
    }

    public void setSlotsBalance(int slotsBalance) {

        this.slotsBalance = slotsBalance;
    }

    public int getSlotsOverdraft() {

        return this.slotsOverdraft;
    }

    public void setSlotsOverdraft(int slotsOverdraft) {

        this.slotsOverdraft = slotsOverdraft;
    }

    public int getSlotsPurchased() {

        return this.slotsPurchased;
    }

    public void setSlotsPurchased(int slotsPurchased) {

        this.slotsPurchased = slotsPurchased;
    }

    public int getSlotsWarning() {

        return this.slotsWarning;
    }

    public void setSlotsWarning(int slotsWarning) {

        this.slotsWarning = slotsWarning;
    }

    public String getTradingName() {

        return this.tradingName;
    }

    public void setTradingName(String tradingName) {

        this.tradingName = tradingName;
    }

    public Date getTransitionProcessedOn() {

        return this.transitionProcessedOn;
    }

    public void setTransitionProcessedOn(Date transitionProcessedOn) {

        this.transitionProcessedOn = transitionProcessedOn;
    }

    public Date getTransitionScheduledOn() {

        return this.transitionScheduledOn;
    }

    public void setTransitionScheduledOn(Date transitionScheduledOn) {

        this.transitionScheduledOn = transitionScheduledOn;
    }

    public String getVatRegistrationNumber() {

        return this.vatRegistrationNumber;
    }

    public void setVatRegistrationNumber(String vatRegistrationNumber) {

        this.vatRegistrationNumber = vatRegistrationNumber;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public CompanyType getCompanyType() {

        return this.companyType;
    }

    public void setCompanyType(CompanyType companyType) {

        this.companyType = companyType;
    }

    public OrganisationType getOrganisationType() {

        return this.organisationType;
    }

    public void setOrganisationType(OrganisationType organisationType) {

        this.organisationType = organisationType;
    }

    public List<Site> getSites() {

        return this.sites;
    }

    public void setSites(List<Site> sites) {

        this.sites = sites;
    }

    public Site addSite(Site site) {

        getSites().add(site);
        site.setOrganisation(this);

        return site;
    }

    public Site removeSite(Site site) {

        getSites().remove(site);
        site.setOrganisation(null);

        return site;
    }

  /*
   * public List<AuthForAe> getAuthForAes() { return this.authForAes; }
   * 
   * public void setAuthForAes( List<AuthForAe> authForAes ) { this.authForAes =
   * authForAes; }
   * 
   * public AuthForAe addAuthForAe( AuthForAe authForAe ) { getAuthForAes().add(
   * authForAe ); authForAe.setOrganisation( this );
   * 
   * return authForAe; }
   * 
   * public AuthForAe removeAuthForAe( AuthForAe authForAe ) {
   * getAuthForAes().remove( authForAe ); authForAe.setOrganisation( null );
   * 
   * return authForAe; }
   * 
   * public List<DirectDebit> getDirectDebits() { return this.directDebits; }
   * 
   * public void setDirectDebits( List<DirectDebit> directDebits ) {
   * this.directDebits = directDebits; }
   * 
   * public DirectDebit addDirectDebit( DirectDebit directDebit ) {
   * getDirectDebits().add( directDebit ); directDebit.setOrganisation( this );
   * 
   * return directDebit; }
   * 
   * public DirectDebit removeDirectDebit( DirectDebit directDebit ) {
   * getDirectDebits().remove( directDebit ); directDebit.setOrganisation( null
   * );
   * 
   * return directDebit; }
   * 
   * public List<EventOrganisationMap> getEventOrganisationMaps() { return
   * this.eventOrganisationMaps; }
   * 
   * public void setEventOrganisationMaps( List<EventOrganisationMap>
   * eventOrganisationMaps ) { this.eventOrganisationMaps =
   * eventOrganisationMaps; }
   * 
   * public EventOrganisationMap addEventOrganisationMap( EventOrganisationMap
   * eventOrganisationMap ) { getEventOrganisationMaps().add(
   * eventOrganisationMap ); eventOrganisationMap.setOrganisation( this );
   * 
   * return eventOrganisationMap; }
   * 
   * public EventOrganisationMap removeEventOrganisationMap(
   * EventOrganisationMap eventOrganisationMap ) {
   * getEventOrganisationMaps().remove( eventOrganisationMap );
   * eventOrganisationMap.setOrganisation( null );
   * 
   * return eventOrganisationMap; }
   * 
   * public TransitionStatus getTransitionStatus() { return
   * this.transitionStatus; }
   * 
   * public void setTransitionStatus( TransitionStatus transitionStatus ) {
   * this.transitionStatus = transitionStatus; }
   * 
   * public List<OrganisationAssemblyRoleMap> getOrganisationAssemblyRoleMaps()
   * { return this.organisationAssemblyRoleMaps; }
   * 
   * public void setOrganisationAssemblyRoleMaps(
   * List<OrganisationAssemblyRoleMap> organisationAssemblyRoleMaps ) {
   * this.organisationAssemblyRoleMaps = organisationAssemblyRoleMaps; }
   * 
   * public OrganisationAssemblyRoleMap addOrganisationAssemblyRoleMap(
   * OrganisationAssemblyRoleMap organisationAssemblyRoleMap ) {
   * getOrganisationAssemblyRoleMaps().add( organisationAssemblyRoleMap );
   * organisationAssemblyRoleMap.setOrganisation( this );
   * 
   * return organisationAssemblyRoleMap; }
   * 
   * public OrganisationAssemblyRoleMap removeOrganisationAssemblyRoleMap(
   * OrganisationAssemblyRoleMap organisationAssemblyRoleMap ) {
   * getOrganisationAssemblyRoleMaps().remove( organisationAssemblyRoleMap );
   * organisationAssemblyRoleMap.setOrganisation( null );
   * 
   * return organisationAssemblyRoleMap; }
   * 
   * public List<OrganisationBusinessRoleMap> getOrganisationBusinessRoleMaps()
   * { return this.organisationBusinessRoleMaps; }
   * 
   * public void setOrganisationBusinessRoleMaps(
   * List<OrganisationBusinessRoleMap> organisationBusinessRoleMaps ) {
   * this.organisationBusinessRoleMaps = organisationBusinessRoleMaps; }
   * 
   * public OrganisationBusinessRoleMap addOrganisationBusinessRoleMap(
   * OrganisationBusinessRoleMap organisationBusinessRoleMap ) {
   * getOrganisationBusinessRoleMaps().add( organisationBusinessRoleMap );
   * organisationBusinessRoleMap.setOrganisation( this );
   * 
   * return organisationBusinessRoleMap; }
   * 
   * public OrganisationBusinessRoleMap removeOrganisationBusinessRoleMap(
   * OrganisationBusinessRoleMap organisationBusinessRoleMap ) {
   * getOrganisationBusinessRoleMaps().remove( organisationBusinessRoleMap );
   * organisationBusinessRoleMap.setOrganisation( null );
   * 
   * return organisationBusinessRoleMap; }
   * 
   * public List<OrganisationContactDetailMap>
   * getOrganisationContactDetailMaps() { return
   * this.organisationContactDetailMaps; }
   * 
   * public void setOrganisationContactDetailMaps(
   * List<OrganisationContactDetailMap> organisationContactDetailMaps ) {
   * this.organisationContactDetailMaps = organisationContactDetailMaps; }
   * 
   * public OrganisationContactDetailMap addOrganisationContactDetailMap(
   * OrganisationContactDetailMap organisationContactDetailMap ) {
   * getOrganisationContactDetailMaps().add( organisationContactDetailMap );
   * organisationContactDetailMap.setOrganisation( this );
   * 
   * return organisationContactDetailMap; }
   * 
   * public OrganisationContactDetailMap removeOrganisationContactDetailMap(
   * OrganisationContactDetailMap organisationContactDetailMap ) {
   * getOrganisationContactDetailMaps().remove( organisationContactDetailMap );
   * organisationContactDetailMap.setOrganisation( null );
   * 
   * return organisationContactDetailMap; }
   * 
   * public List<OrganisationSiteMap> getOrganisationSiteMaps() { return
   * this.organisationSiteMaps; }
   * 
   * public void setOrganisationSiteMaps( List<OrganisationSiteMap>
   * organisationSiteMaps ) { this.organisationSiteMaps = organisationSiteMaps;
   * }
   * 
   * public OrganisationSiteMap addOrganisationSiteMap( OrganisationSiteMap
   * organisationSiteMap ) { getOrganisationSiteMaps().add( organisationSiteMap
   * ); organisationSiteMap.setOrganisation( this );
   * 
   * return organisationSiteMap; }
   * 
   * public OrganisationSiteMap removeOrganisationSiteMap( OrganisationSiteMap
   * organisationSiteMap ) { getOrganisationSiteMaps().remove(
   * organisationSiteMap ); organisationSiteMap.setOrganisation( null );
   * 
   * return organisationSiteMap; }
   * 
   * public List<TestSlotTransaction> getTestSlotTransactions() { return
   * this.testSlotTransactions; }
   * 
   * public void setTestSlotTransactions( List<TestSlotTransaction>
   * testSlotTransactions ) { this.testSlotTransactions = testSlotTransactions;
   * }
   * 
   * public TestSlotTransaction addTestSlotTransaction( TestSlotTransaction
   * testSlotTransaction ) { getTestSlotTransactions().add( testSlotTransaction
   * ); testSlotTransaction.setOrganisation( this );
   * 
   * return testSlotTransaction; }
   * 
   * public TestSlotTransaction removeTestSlotTransaction( TestSlotTransaction
   * testSlotTransaction ) { getTestSlotTransactions().remove(
   * testSlotTransaction ); testSlotTransaction.setOrganisation( null );
   * 
   * return testSlotTransaction; }
   * 
   * public List<TestSlotTransactionAmendment>
   * getTestSlotTransactionAmendments() { return
   * this.testSlotTransactionAmendments; }
   * 
   * public void setTestSlotTransactionAmendments(
   * List<TestSlotTransactionAmendment> testSlotTransactionAmendments ) {
   * this.testSlotTransactionAmendments = testSlotTransactionAmendments; }
   * 
   * public TestSlotTransactionAmendment addTestSlotTransactionAmendment(
   * TestSlotTransactionAmendment testSlotTransactionAmendment ) {
   * getTestSlotTransactionAmendments().add( testSlotTransactionAmendment );
   * testSlotTransactionAmendment.setOrganisation( this );
   * 
   * return testSlotTransactionAmendment; }
   * 
   * public TestSlotTransactionAmendment removeTestSlotTransactionAmendment(
   * TestSlotTransactionAmendment testSlotTransactionAmendment ) {
   * getTestSlotTransactionAmendments().remove( testSlotTransactionAmendment );
   * testSlotTransactionAmendment.setOrganisation( null );
   * 
   * return testSlotTransactionAmendment; }
   */
}