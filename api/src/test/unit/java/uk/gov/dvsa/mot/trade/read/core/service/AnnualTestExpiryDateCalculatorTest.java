package uk.gov.dvsa.mot.trade.read.core.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import uk.gov.dvsa.mot.trade.service.AnnualTestExpiryDateCalculator;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import static com.googlecode.catchexception.CatchException.catchException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class AnnualTestExpiryDateCalculatorTest {
    private static final String HGV_VEHICLE_TYPE = "HGV";
    private static final String PSV_VEHICLE_TYPE = "PSV";
    private static final String TRAILER_VEHICLE_TYPE = "Trailer";

    private AnnualTestExpiryDateCalculator annualTestExpiryDateCalculator;

    @Before
    public void setUp() {
        annualTestExpiryDateCalculator = new AnnualTestExpiryDateCalculator();
    }

    @Test
    public void whenExpiryDateIsUnknown_returnAbsent() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(HGV_VEHICLE_TYPE);
        vehicle.setTestCertificateExpiryDate(null);

        assertFalse(annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle).isPresent());
    }

    @Test
    public void whenExpiryDateIsUnknownForTrailer_returnAbsent() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(TRAILER_VEHICLE_TYPE);
        vehicle.setTestCertificateExpiryDate(null);

        assertFalse(annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle).isPresent());
    }

    @Test
    @UseDataProvider("dataProviderForHgvVehicleRegDateAndExpectedAnnualTestExpiryDate")
    public void whenHgvVehicleWithoutTestCertificateExpiryDate(String registrationDate, String expectedAnnualTestDate) throws Exception {
        Vehicle vehicle = defaultHgvVehicleWithRegistrationDate(HGV_VEHICLE_TYPE, registrationDate);

        Optional<LocalDate> annualTestExpiryDate = annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle);

        assertTrue(annualTestExpiryDate.isPresent());
        assertEquals(LocalDate.parse(expectedAnnualTestDate), annualTestExpiryDate.get());
    }

    @Test
    public void whenTrailerWithoutTestCertificateExpiryDate_returnAbsent() throws Exception {
        Vehicle vehicle = defaultHgvVehicleWithRegistrationDate(TRAILER_VEHICLE_TYPE, "31/03/2017");

        assertFalse(annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle).isPresent());
    }

    @Test
    @UseDataProvider("dataProviderForPsvVehicleRegDateAndExpectedAnnualTestExpiryDate")
    public void whenPsvVehicleWithoutTestCertificateExpiryDate(String registrationDate, String expectedAnnualTestDate) throws Exception {
        Vehicle vehicle = defaultHgvVehicleWithRegistrationDate(PSV_VEHICLE_TYPE, registrationDate);

        Optional<LocalDate> annualTestExpiryDate = annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle);

        assertTrue(annualTestExpiryDate.isPresent());
        assertEquals(LocalDate.parse(expectedAnnualTestDate), annualTestExpiryDate.get());
    }

    @Test
    public void whenVehicleWithTestCertificateExpiryDate() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setTestCertificateExpiryDate("28/01/2016");

        Optional<LocalDate> annualTestExpiryDate = annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle);

        assertTrue(annualTestExpiryDate.isPresent());
        assertEquals(LocalDate.of(2016, 1, 28), annualTestExpiryDate.get());
    }

    @Test(expected = DateTimeParseException.class)
    @UseDataProvider("dataProviderForInvalidDate")
    public void whenVehicleWithIncorrectTestCertificateExpiryDate(String expiryDate) throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setTestCertificateExpiryDate(expiryDate);

        annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle);
    }

    private Vehicle defaultHgvVehicleWithRegistrationDate(String vehicleType, String registrationDate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(vehicleType);
        vehicle.setRegistrationDate(registrationDate);

        return vehicle;
    }

    @DataProvider
    public static Object[][] dataProviderForHgvVehicleRegDateAndExpectedAnnualTestExpiryDate() {
        return new Object[][]{
                {"31/03/2017", "2018-03-31"},
                {"29/02/2016", "2017-02-28"},
                {"05/12/2011", "2012-12-31"},
                {"01/10/2019", "2020-10-31"},
                {"9/05/2019", "2020-05-31"}
        };
    }

    @DataProvider
    public static Object[][] dataProviderForPsvVehicleRegDateAndExpectedAnnualTestExpiryDate() {
        return new Object[][]{
                {"31/03/2017", "2018-03-31"},
                {"29/02/2016", "2017-02-28"},
                {"05/12/2011", "2012-12-05"},
                {"01/10/2019", "2020-10-01"},
                {"1/1/2019", "2020-01-01"}
        };
    }

    @DataProvider
    public static Object[][] dataProviderForInvalidDate() {
        return new Object[][]{
                {"32/07/2017"},
                {"01/13/2017"},
                {"13/2017"},
                {"0/0/2014"}
        };
    }
}
