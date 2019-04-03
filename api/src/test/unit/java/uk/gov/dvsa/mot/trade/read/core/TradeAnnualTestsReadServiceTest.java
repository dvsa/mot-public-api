package uk.gov.dvsa.mot.trade.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.gov.dvsa.mot.trade.api.response.mapper.MockVehicleDataHelper;
import uk.gov.dvsa.mot.vehicle.hgv.SearchVehicleProvider;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TradeAnnualTestsReadServiceTest {

    private TradeAnnualTestsReadService tradeAnnualTestsReadService;

    @Mock
    SearchVehicleProvider searchVehicleProviderMock;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        this.tradeAnnualTestsReadService = new TradeAnnualTestsReadService();
        this.tradeAnnualTestsReadService.setSearchVehicleProvider(this.searchVehicleProviderMock);
    }

    @After
    public void afterTest() {
        this.tradeAnnualTestsReadService = null;
    }

    @Test
    public void getAnnualTests_SingleRegistration() throws Exception {

        HashSet<String> registrations = new HashSet<String>() {
            {
                add("ABC123");
            }
        };

        for (String registration : registrations) {
            Vehicle vehicle = MockVehicleDataHelper.getSearchVehicle("HGV", false);
            vehicle.setVehicleIdentifier(registration);

            when(searchVehicleProviderMock.getVehicle(registration)).thenReturn(vehicle);
        }

        List<Vehicle> vehiclesResponse = this.tradeAnnualTestsReadService.getAnnualTests(registrations);

        for (String registration : registrations) {
            verify(searchVehicleProviderMock).getVehicle(registration);

            Optional<Vehicle> returnedVehicle = vehiclesResponse.stream().filter(
                    v -> v.getVehicleIdentifier().equals(registration)
            ).findFirst();

            assertNotNull(returnedVehicle);

            assertEquals(
                    "The vehicle returned has the same VRM",
                    registration,
                    returnedVehicle.orElse(new Vehicle()).getVehicleIdentifier()
            );
        }

        verify(searchVehicleProviderMock, times(1)).getVehicle(any());
        assertEquals("We return one vehicle", 1, vehiclesResponse.size());
    }

    @Test
    public void getAnnualTests_MultipleRegistrations() throws Exception {

        HashSet<String> registrations = new HashSet<String>() {
            {
                add("ABC123");
                add("CBA321");
                add("BAC231");
                add("ACB312");
            }
        };

        for (String registration : registrations) {
            Vehicle vehicle = MockVehicleDataHelper.getSearchVehicle("HGV", false);
            vehicle.setVehicleIdentifier(registration);

            when(searchVehicleProviderMock.getVehicle(registration)).thenReturn(vehicle);
        }

        List<Vehicle> vehiclesResponse = this.tradeAnnualTestsReadService.getAnnualTests(registrations);

        for (String registration : registrations) {
            verify(searchVehicleProviderMock).getVehicle(registration);

            Optional<Vehicle> returnedVehicle = vehiclesResponse.stream().filter(
                    v -> v.getVehicleIdentifier().equals(registration)
            ).findFirst();

            assertNotNull(returnedVehicle);

            assertEquals(
                    "The vehicle returned has the same VRM",
                    registration,
                    returnedVehicle.orElse(new Vehicle()).getVehicleIdentifier()
            );
        }

        verify(searchVehicleProviderMock, times(4)).getVehicle(any());
        assertEquals("We return all 4 vehicles", 4, vehiclesResponse.size());
    }

    @Test
    public void getAnnualTests_EmptyRegistrationsAreIgnored() throws Exception {

        HashSet<String> registrations = new HashSet<String>() {
            {
                add("ABC123");
                add("");
                add("CBA321");
            }
        };

        for (String registration : registrations) {
            Vehicle vehicle = MockVehicleDataHelper.getSearchVehicle("HGV", false);
            vehicle.setVehicleIdentifier(registration);

            when(searchVehicleProviderMock.getVehicle(registration)).thenReturn(vehicle);
        }

        List<Vehicle> vehiclesResponse = this.tradeAnnualTestsReadService.getAnnualTests(registrations);

        for (String registration : registrations) {
            if (!registration.equals("")) {
                verify(searchVehicleProviderMock).getVehicle(registration);
            }
        }

        verify(searchVehicleProviderMock, times(2)).getVehicle(any());
        assertEquals("We return two vehicles only", 2, vehiclesResponse.size());
    }

    @Test
    public void getAnnualTests_VehiclesNotFoundAreOmittedFromResultSet() throws Exception {

        HashSet<String> registrations = new HashSet<String>() {
            {
                add("ABC123");
                add("NOSUCH");
                add("CBA321");
                add("BCA131");
            }
        };

        for (String registration : registrations) {
            Vehicle vehicle = MockVehicleDataHelper.getSearchVehicle("HGV", false);
            vehicle.setVehicleIdentifier(registration);

            if (registration.equals("NOSUCH")) {
                when(searchVehicleProviderMock.getVehicle(registration)).thenReturn(null);
            } else {
                when(searchVehicleProviderMock.getVehicle(registration)).thenReturn(vehicle);
            }
        }

        List<Vehicle> vehiclesResponse = this.tradeAnnualTestsReadService.getAnnualTests(registrations);

        for (String registration : registrations) {
            verify(searchVehicleProviderMock).getVehicle(registration);
        }

        verify(searchVehicleProviderMock, times(4)).getVehicle(any());
        assertEquals("We return three vehicles", 3, vehiclesResponse.size());
    }
}