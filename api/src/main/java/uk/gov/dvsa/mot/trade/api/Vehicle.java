package uk.gov.dvsa.mot.trade.api;

import java.util.List;

public class Vehicle {
    protected Integer id;
    protected String registration;
    protected String make;
    protected String model;
    protected String makeInFull;
    protected String firstUsedDate;
    protected String manufactureDate;
    protected String manufactureYear;
    protected String fuelType;
    protected String primaryColour;
    protected String secondaryColour;
    protected String motTestDueDate;
    protected String motTestNumber;
    protected List<MotTest> motTests;
    protected String dvlaId;

    public String getRegistration() {

        return registration;
    }

    public void setRegistration(String registration) {

        this.registration = registration;
    }

    public String getMake() {

        return make;
    }

    public void setMake(String make) {

        this.make = make;
    }

    public String getModel() {

        return model;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public String getMakeInFull() {

        return makeInFull;
    }

    public void setMakeInFull(String makeInFull) {

        this.makeInFull = makeInFull;
    }

    public String getFirstUsedDate() {

        return firstUsedDate;
    }

    public void setFirstUsedDate(String firstUsedDate) {

        this.firstUsedDate = firstUsedDate;
    }

    public String getFuelType() {

        return fuelType;
    }

    public void setFuelType(String fuelType) {

        this.fuelType = fuelType;
    }

    public String getPrimaryColour() {

        return primaryColour;
    }

    public void setPrimaryColour(String primaryColour) {

        this.primaryColour = primaryColour;
    }

    public List<MotTest> getMotTests() {

        return motTests;
    }

    public void setMotTests(List<MotTest> motTests) {

        this.motTests = motTests;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getManufactureDate() {

        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {

        this.manufactureDate = manufactureDate;
    }

    public String getSecondaryColour() {

        return secondaryColour;
    }

    public void setSecondaryColour(String secondaryColour) {

        this.secondaryColour = secondaryColour;
    }

    public String getManufactureYear() {

        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {

        this.manufactureYear = manufactureYear;
    }

    public String getMotTestDueDate() {

        return motTestDueDate;
    }

    public void setMotTestDueDate(String motTestDueDate) {

        this.motTestDueDate = motTestDueDate;
    }

    public String getMotTestNumber() {

        return motTestNumber;
    }

    public void setMotTestNumber(String motTestNumber) {

        this.motTestNumber = motTestNumber;
    }

    public String getDvlaId() {

        return dvlaId;
    }

    public void setDvlaId(String dvlaId) {

        this.dvlaId = dvlaId;
    }
}
