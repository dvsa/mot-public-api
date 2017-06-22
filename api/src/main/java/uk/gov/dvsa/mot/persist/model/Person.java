package uk.gov.dvsa.mot.persist.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the person database table.
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Gender gender;
    private Title title;
    private String firstName;
    private String middleName;
    private String familyName;
    private Date dateOfBirth;
    private boolean isDeceased;
    private Date deceasedOn;
    private Licence licence;
    private String username;
    private String userReference;
    private String disability;
    private int otpFailedAttempts;
    private int demoTestTesterStatusId;
    private Date detailsConfirmedOn;
    private Date firstLiveTestDoneOn;
    private Date firstTrainingTestDoneOn;
    private boolean isAccountClaimRequired;
    private boolean isPasswordChangeRequired;
    private PersonAuthTypeLookup personAuthTypeLookup;
    private String pin;
    private String faTokenId;
    private Date faTokenSentOn;
    private String motOneUserId;
    private String mot1UserId;
    private Date mot1DetailsUpdatedOn;
    private String mot1CurrentSmartcardId;
    private int createdBy;
    private Date createdOn;
    private int lastUpdatedBy;
    private Date lastUpdatedOn;
    private int version;

    public Person() {

    }

    public int getId() {

        return this.id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getFaTokenId() {

        return this.faTokenId;
    }

    public void setFaTokenId(String faTokenId) {

        this.faTokenId = faTokenId;
    }

    public Date getFaTokenSentOn() {

        return this.faTokenSentOn;
    }

    public void setFaTokenSentOn(Date faTokenSentOn) {

        this.faTokenSentOn = faTokenSentOn;
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

    public Date getDateOfBirth() {

        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {

        this.dateOfBirth = dateOfBirth;
    }

    public Date getDeceasedOn() {

        return this.deceasedOn;
    }

    public void setDeceasedOn(Date deceasedOn) {

        this.deceasedOn = deceasedOn;
    }

    public int getDemoTestTesterStatusId() {

        return this.demoTestTesterStatusId;
    }

    public void setDemoTestTesterStatusId(int demoTestTesterStatusId) {

        this.demoTestTesterStatusId = demoTestTesterStatusId;
    }

    public Date getDetailsConfirmedOn() {

        return this.detailsConfirmedOn;
    }

    public void setDetailsConfirmedOn(Date detailsConfirmedOn) {

        this.detailsConfirmedOn = detailsConfirmedOn;
    }

    public String getDisability() {

        return this.disability;
    }

    public void setDisability(String disability) {

        this.disability = disability;
    }

    public String getFamilyName() {

        return this.familyName;
    }

    public void setFamilyName(String familyName) {

        this.familyName = familyName;
    }

    public Date getFirstLiveTestDoneOn() {

        return this.firstLiveTestDoneOn;
    }

    public void setFirstLiveTestDoneOn(Date firstLiveTestDoneOn) {

        this.firstLiveTestDoneOn = firstLiveTestDoneOn;
    }

    public String getFirstName() {

        return this.firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public Date getFirstTrainingTestDoneOn() {

        return this.firstTrainingTestDoneOn;
    }

    public void setFirstTrainingTestDoneOn(Date firstTrainingTestDoneOn) {

        this.firstTrainingTestDoneOn = firstTrainingTestDoneOn;
    }

    public boolean getIsAccountClaimRequired() {

        return this.isAccountClaimRequired;
    }

    public void setIsAccountClaimRequired(boolean isAccountClaimRequired) {

        this.isAccountClaimRequired = isAccountClaimRequired;
    }

    public boolean getIsDeceased() {

        return this.isDeceased;
    }

    public void setIsDeceased(boolean isDeceased) {

        this.isDeceased = isDeceased;
    }

    public boolean getIsPasswordChangeRequired() {

        return this.isPasswordChangeRequired;
    }

    public void setIsPasswordChangeRequired(boolean isPasswordChangeRequired) {

        this.isPasswordChangeRequired = isPasswordChangeRequired;
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

    public String getMiddleName() {

        return this.middleName;
    }

    public void setMiddleName(String middleName) {

        this.middleName = middleName;
    }

    public String getMotOneUserId() {

        return this.motOneUserId;
    }

    public void setMotOneUserId(String motOneUserId) {

        this.motOneUserId = motOneUserId;
    }

    public String getMot1CurrentSmartcardId() {

        return this.mot1CurrentSmartcardId;
    }

    public void setMot1CurrentSmartcardId(String mot1CurrentSmartcardId) {

        this.mot1CurrentSmartcardId = mot1CurrentSmartcardId;
    }

    public Date getMot1DetailsUpdatedOn() {

        return this.mot1DetailsUpdatedOn;
    }

    public void setMot1DetailsUpdatedOn(Date mot1DetailsUpdatedOn) {

        this.mot1DetailsUpdatedOn = mot1DetailsUpdatedOn;
    }

    public String getMot1UserId() {

        return this.mot1UserId;
    }

    public void setMot1UserId(String mot1UserId) {

        this.mot1UserId = mot1UserId;
    }

    public int getOtpFailedAttempts() {

        return this.otpFailedAttempts;
    }

    public void setOtpFailedAttempts(int otpFailedAttempts) {

        this.otpFailedAttempts = otpFailedAttempts;
    }

    public String getPin() {

        return this.pin;
    }

    public void setPin(String pin) {

        this.pin = pin;
    }

    public String getUserReference() {

        return this.userReference;
    }

    public void setUserReference(String userReference) {

        this.userReference = userReference;
    }

    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public int getVersion() {

        return this.version;
    }

    public void setVersion(int version) {

        this.version = version;
    }

    public Licence getLicence() {

        return this.licence;
    }

    public void setLicence(Licence licence) {

        this.licence = licence;
    }

    public PersonAuthTypeLookup getPersonAuthTypeLookup() {

        return this.personAuthTypeLookup;
    }

    public void setPersonAuthTypeLookup(PersonAuthTypeLookup personAuthTypeLookup) {

        this.personAuthTypeLookup = personAuthTypeLookup;
    }

    public Gender getGender() {

        return this.gender;
    }

    public void setGender(Gender gender) {

        this.gender = gender;
    }

    public Title getTitle() {

        return this.title;
    }

    public void setTitle(Title title) {

        this.title = title;
    }
}