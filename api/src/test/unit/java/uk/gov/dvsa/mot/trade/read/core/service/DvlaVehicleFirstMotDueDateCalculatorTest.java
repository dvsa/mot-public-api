package uk.gov.dvsa.mot.trade.read.core.service;

import org.junit.Before;
import org.junit.Test;

import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.trade.service.DvlaVehicleFirstMotDueDateCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DvlaVehicleFirstMotDueDateCalculatorTest {

    private static final String M1 = "M1";
    private static final String M2 = "M2";
    private static final String M3 = "M3";
    private static final String N1 = "N1";
    private static final String L1 = "L1";
    private static final String L2 = "L2";
    private static final String L3 = "L3";
    private static final String L4 = "L4";
    private static final String L5 = "L5";
    private static final String L6 = "L6";

    private static final int THREE_YEARS = 3;
    private static final int ONE_YEAR = 1;
    private static final int MINUS_ONE_DAY = -1;
    private static final String AMBULANCE_BODY_TYPE = "76";
    private static final String TAXI_BODY_TYPE = "07";
    private static final String BULLDOZER_BODY_TYPE = "78";

    private Date dateOfManufacture;
    private Date dateOfManufactureResultingInCalculatedDueDateOnLeapDay;
    private DvlaVehicle dvlaVehicle;

    @Before
    public void setUp() {
        dateOfManufacture = new GregorianCalendar(2017, Calendar.JULY, 31).getTime();
        dateOfManufactureResultingInCalculatedDueDateOnLeapDay = new GregorianCalendar(2019, Calendar.MARCH, 1).getTime();
        dvlaVehicle = mock(DvlaVehicle.class);
    }

    @Test
    public void testEuClassM1IsTaxiHasDueDateOfOneYearMinusOneDayAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(TAXI_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(M1);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, ONE_YEAR));
    }

    @Test
    public void testEuClassM1IsAmbulanceHasDueDateOfOneYearMinusOneDayAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(AMBULANCE_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(M1);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, ONE_YEAR));
    }

    @Test
    public void testEuClassM1IsNotAmbulanceOrTaxiHasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(M1);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassM2HasDueDateOfOneYearAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(M2);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, ONE_YEAR));
    }

    @Test
    public void testEuClassM3HasDueDateOfOneYearAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(M3);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, ONE_YEAR));
    }

    @Test
    public void testEuClassN1HasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(N1);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassesL1THasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(L1);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassesL2THasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(L2);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassesL3THasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(L3);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassesL4THasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(L4);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassesL5THasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(L5);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testEuClassesL6THasDueDateOfThreeYearsAfterFirstRegistration() {

        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(L6);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufacture);

        assertEquals(DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle),
                getFutureDueDate(dateOfManufacture, THREE_YEARS));
    }

    @Test
    public void testLeapYearsAreHandledCorrectly() {
        /*
          When a vehicle's manufactured date falls on a date that would result
          in its calculated MOT test due date falling on the 29th of February,
          the calculator should indeed correctly return the 29th of February.
         */
        when(dvlaVehicle.getBodyTypeCode()).thenReturn(BULLDOZER_BODY_TYPE);
        when(dvlaVehicle.getEuClassification()).thenReturn(M2);
        when(dvlaVehicle.getFirstRegistrationDate()).thenReturn(dateOfManufactureResultingInCalculatedDueDateOnLeapDay);

        Date twentyNinthFeb = new GregorianCalendar(2020, Calendar.FEBRUARY, 29).getTime();

        assertEquals(twentyNinthFeb, DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle));
    }

    /**
     * Returns a date a provided number of years in the future
     *
     * @param originalDate  the date to add years to
     * @param yearsInFuture the number of years to add to originalDate
     * @return the future date
     */
    private Date getFutureDueDate(Date originalDate, int yearsInFuture) {
        Calendar futureDueDate = Calendar.getInstance();
        futureDueDate.setTime(originalDate);
        futureDueDate.add(Calendar.YEAR, yearsInFuture);
        futureDueDate.add(Calendar.DATE, MINUS_ONE_DAY);

        return futureDueDate.getTime();
    }
}
