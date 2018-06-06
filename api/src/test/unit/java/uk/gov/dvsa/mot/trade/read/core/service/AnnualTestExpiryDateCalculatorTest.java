package uk.gov.dvsa.mot.trade.read.core.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.trade.service.AnnualTestExpiryDateCalculator;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.format.DateTimeParseException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class AnnualTestExpiryDateCalculatorTest {
    private static final String HGV_VEHICLE_TYPE = "HGV";
    private static final String PSV_VEHICLE_TYPE = "PSV";

    private AnnualTestExpiryDateCalculator annualTestExpiryDateCalculator;

    @Before
    public void setUp() {
        annualTestExpiryDateCalculator = new AnnualTestExpiryDateCalculator();
    }

    @Test
    public void whenVehicleWithoutTestCertificateExpiryDateAndRegistrationDate() throws Exception {
        Vehicle vehicle = new Vehicle();

        catchException(annualTestExpiryDateCalculator).determineAnnualTestExpiryDate(vehicle, "1");

        assertThat(caughtException(), allOf(
                instanceOf(BadRequestException.class),
                hasMessageThat(containsString("Registration date is null or empty")),
                hasNoCause()
        ));
    }

    @Test
    @UseDataProvider("dataProviderForHgvVehicleRegDateAndExpectedAnnualTestExpiryDate")
    public void whenHgvVehicleWithoutTestCertificateExpiryDate(String registrationDate, String expectedAnnualTestDate) throws Exception {
        Vehicle vehicle = defaultHgvVehicleWithRegistrationDate(HGV_VEHICLE_TYPE, registrationDate);

        String annualTestExpiryCate = annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle, "1");

        assertTrue(annualTestExpiryCate.equals(expectedAnnualTestDate));
    }

    @Test
    @UseDataProvider("dataProviderForPsvVehicleRegDateAndExpectedAnnualTestExpiryDate")
    public void whenPsvVehicleWithoutTestCertificateExpiryDate(String registrationDate, String expectedAnnualTestDate) throws Exception {
        Vehicle vehicle = defaultHgvVehicleWithRegistrationDate(PSV_VEHICLE_TYPE, registrationDate);

        String annualTestExpiryCate = annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle, "1");

        assertTrue(annualTestExpiryCate.equals(expectedAnnualTestDate));
    }

    @Test
    public void whenVehicleWithTestCertificateExpiryDate() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setTestCertificateExpiryDate("28/01/2016");

        String annualTestExpiryCate = annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle, "1");

        assertTrue(annualTestExpiryCate.equals("2016-01-28"));
    }

    @Test(expected = DateTimeParseException.class)
    @UseDataProvider("dataProviderForInvalidDate")
    public void whenVehicleWithIncorrectTestCertificateExpiryDate(String expiryDate) throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setTestCertificateExpiryDate(expiryDate);

        annualTestExpiryDateCalculator.determineAnnualTestExpiryDate(vehicle, "1");
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
