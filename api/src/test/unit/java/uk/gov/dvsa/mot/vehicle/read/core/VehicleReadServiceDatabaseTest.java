package uk.gov.dvsa.mot.vehicle.read.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.dvsa.mot.persist.VehicleReadDao;
import uk.gov.dvsa.mot.persist.model.BodyType;
import uk.gov.dvsa.mot.persist.model.ColourLookup;
import uk.gov.dvsa.mot.persist.model.DvlaMake;
import uk.gov.dvsa.mot.persist.model.DvlaModel;
import uk.gov.dvsa.mot.persist.model.FuelType;
import uk.gov.dvsa.mot.persist.model.Make;
import uk.gov.dvsa.mot.persist.model.ModelDetail;
import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static uk.gov.dvsa.mot.test.utility.Matchers.hasSize;
import static uk.gov.dvsa.mot.test.utility.Matchers.isEmpty;

@RunWith(MockitoJUnitRunner.class)
public class VehicleReadServiceDatabaseTest {

    private static final Long DEFAULT_VEHICLE_ID = 1234567L;
    private static final String DEFAULT_REGISTRATION = "ABC123DE";
    private static final String DEFAULT_BODY_TYPE_CODE = "testBodyTypeCode";
    private static final String DEFAULT_COLOR_NAME = "testColor";
    private static final String DEFAULT_FUEL_TYPE_NAME = "testFuelType";
    private static final String DEFAULT_MODEL_DETAIL_NAME = "testModelDetail";
    private static final String DEFAULT_MAKE_NAME = "testMakeName";


    VehicleReadServiceDatabase vehicleReadServiceDatabase;
    uk.gov.dvsa.mot.persist.model.Vehicle testVehicle;

    @Mock
    VehicleReadDao vehicleReadDaoMock;

    @Before
    public void beforeTest() {
        // Arrange - Create class under test
        vehicleReadServiceDatabase = new VehicleReadServiceDatabase(vehicleReadDaoMock);
    }

    @After
    public void afterTest() {

        vehicleReadServiceDatabase = null;
    }

    private List<uk.gov.dvsa.mot.persist.model.Vehicle> getVehicleList() {
        // Arrange - Set-up mocks
        uk.gov.dvsa.mot.persist.model.Vehicle vehicle = mock(uk.gov.dvsa.mot.persist.model.Vehicle.class,
                RETURNS_DEEP_STUBS);
        List<uk.gov.dvsa.mot.persist.model.Vehicle> result = new ArrayList<>(1);
        result.add(vehicle);

        return result;
    }

    // getVehicleById Tests
    // -----------------------

    @Test
    public void getVehicleById_WithNoMatches_ReturnsNull() {
        // Arrange - Set-up mock
        when(vehicleReadDaoMock.getVehicleById(anyInt())).thenReturn(null);

        // Act
        Vehicle actual = vehicleReadServiceDatabase.getVehicleById(anyInt());

        // Assert - Check the response
        assertThat(actual, nullValue());
    }

    @Test
    public void getVehicleById_WithMatch_ReturnsVehicle() {

        uk.gov.dvsa.mot.persist.model.Vehicle vehicle = mock(uk.gov.dvsa.mot.persist.model.Vehicle.class,
                RETURNS_DEEP_STUBS);
        when(vehicleReadDaoMock.getVehicleById(anyInt())).thenReturn(vehicle);

        Vehicle actual = vehicleReadServiceDatabase.getVehicleById(anyInt());

        assertThat(actual, notNullValue());
    }

    @Test
    public void getVehicleByIdAndVersion_WithNoMatches_ReturnsNull() {

        when(vehicleReadDaoMock.getVehicleByIdAndVersion(anyInt(), anyInt())).thenReturn(null);

        Vehicle actual = vehicleReadServiceDatabase.getVehicleByIdAndVersion(1, 1);

        assertNull(actual);
    }

    @Test
    public void getVehicleByIdAndVersion_WithMatch_ReturnsVehicle() {

        uk.gov.dvsa.mot.persist.model.Vehicle vehicle = mock(uk.gov.dvsa.mot.persist.model.Vehicle.class,
                RETURNS_DEEP_STUBS);
        when(vehicleReadDaoMock.getVehicleByIdAndVersion(anyInt(), anyInt())).thenReturn(vehicle);

        Vehicle actual = vehicleReadServiceDatabase.getVehicleByIdAndVersion(1, 1);

        assertNotNull(actual);
    }

    @Test
    public void getVehiclesById_WithNoMatches_ReturnsEmptyList() {

        List<uk.gov.dvsa.mot.persist.model.Vehicle> vehicleList = new ArrayList<>();
        when(vehicleReadDaoMock.getVehiclesById(anyInt(), anyInt())).thenReturn(vehicleList);

        List<Vehicle> actual = vehicleReadServiceDatabase.getVehiclesById(1, 2);

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void getVehiclesById_WithMatch_ReturnsVehicle() {

        List<uk.gov.dvsa.mot.persist.model.Vehicle> vehicleList = getVehicleList();
        when(vehicleReadDaoMock.getVehiclesById(anyInt(), anyInt())).thenReturn(vehicleList);

        List<Vehicle> actual = vehicleReadServiceDatabase.getVehiclesById(1, 2);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(1));

        Vehicle actualItem = actual.get(0);

        assertThat(actualItem, notNullValue());
    }

    @Test
    public void findByRegistrationAndMake_WithNoMatches_ReturnsNull() {

        when(vehicleReadDaoMock.getVehicleByFullRegAndMake(anyString(), anyString())).thenReturn(null);

        Vehicle actual = vehicleReadServiceDatabase.findByRegistrationAndMake("Reg", "Make");

        assertThat(actual, nullValue());
    }

    @Test
    public void findByRegistrationAndMake_WithMatch_ReturnsVehicle() {

        uk.gov.dvsa.mot.persist.model.Vehicle vehicle = mock(uk.gov.dvsa.mot.persist.model.Vehicle.class,
                RETURNS_DEEP_STUBS);
        when(vehicleReadDaoMock.getVehicleByFullRegAndMake(anyString(), anyString())).thenReturn(vehicle);

        Vehicle actual = vehicleReadServiceDatabase.findByRegistrationAndMake("Reg", "Make");

        assertThat(actual, notNullValue());
    }

    @Test
    public void getVehiclesByPage_WithNoMatches_ReturnsEmptyList() {

        List<uk.gov.dvsa.mot.persist.model.Vehicle> vehicleList = new ArrayList<>();
        when(vehicleReadDaoMock.getVehiclesByPage(anyInt(), anyInt())).thenReturn(vehicleList);

        List<Vehicle> actual = vehicleReadServiceDatabase.getVehiclesByPage(1, 2);

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void getVehiclesByPage_WithMatch_ReturnsVehicleList() {

        List<uk.gov.dvsa.mot.persist.model.Vehicle> vehicleList = getVehicleList();
        when(vehicleReadDaoMock.getVehiclesByPage(anyInt(), anyInt())).thenReturn(vehicleList);

        List<Vehicle> actual = vehicleReadServiceDatabase.getVehiclesByPage(1, 2);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(1));

        Vehicle actualItem = actual.get(0);
        assertThat(actualItem, notNullValue());
    }

    @Test
    public void getMakes_WithNoMatches_ReturnsEmptyList() {

        List<Make> makesList = new ArrayList<>();
        when(vehicleReadDaoMock.getMakes()).thenReturn(makesList);

        List<String> actual = vehicleReadServiceDatabase.getMakes();

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void getMakes_WithMatch_ReturnsMakesList() {

        List<Make> makesList = new ArrayList<>();
        Make make = new Make();
        make.setName("TestMake");
        makesList.add(make);
        when(vehicleReadDaoMock.getMakes()).thenReturn(makesList);

        List<String> actual = vehicleReadServiceDatabase.getMakes();

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(1));

        String actualItem = actual.get(0);
        assertThat(actualItem, notNullValue());
        assertThat(actualItem, equalTo(make.getName()));
    }

    /**
     * Create a new ColourLookup object with the specified name.
     *
     * @param name The name of the colour
     * @return A new ColourLookup object with the specified name.
     */
    private ColourLookup createColour(String name) {

        ColourLookup colour = new ColourLookup();
        colour.setName(name);
        return colour;
    }

    private ModelDetail makeModelDetail() {

        return new ModelDetail();
    }

    private uk.gov.dvsa.mot.persist.model.Vehicle makeTestVehicle(String registration, String primaryColourName) {

        uk.gov.dvsa.mot.persist.model.Vehicle vehicle = new uk.gov.dvsa.mot.persist.model.Vehicle();
        vehicle.setRegistration(registration);
        vehicle.setPrimaryColour(createColour(primaryColourName));
        vehicle.setModelDetail(makeModelDetail());
        return vehicle;
    }

    @Test
    public void findByRegistration_ValidVehicles_ReturnsMappedVehicles() {

        final String registration = "HJ89UIK";
        final String colour1 = "INVISIBLE";
        final String colour2 = "POMEGRANATE";

        final uk.gov.dvsa.mot.persist.model.Vehicle vehicle1 = makeTestVehicle(registration, colour1);
        final uk.gov.dvsa.mot.persist.model.Vehicle vehicle2 = makeTestVehicle(registration, colour2);

        final List<uk.gov.dvsa.mot.persist.model.Vehicle> vehiclesList = Arrays.asList(vehicle1, vehicle2);

        when(vehicleReadDaoMock.getVehicleByFullRegistration(registration)).thenReturn(vehiclesList);

        List<Vehicle> actual = vehicleReadServiceDatabase.findByRegistration(registration);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(2));
        assertThat(actual.get(0), notNullValue());
        assertThat(actual.get(0).getPrimaryColour(), equalTo(colour1));
        assertThat(actual.get(0).getRegistration(), equalTo(registration));
        assertThat(actual.get(1), notNullValue());
        assertThat(actual.get(1).getPrimaryColour(), equalTo(colour2));
        assertThat(actual.get(1).getRegistration(), equalTo(registration));
    }

    @Test
    public void findByRegistration_NoVehicles_ReturnsEmptyList() {

        final String registration = "HJ89UIK";

        final List<uk.gov.dvsa.mot.persist.model.Vehicle> vehiclesList = Arrays.asList();

        when(vehicleReadDaoMock.getVehicleByFullRegistration(registration)).thenReturn(vehiclesList);

        List<Vehicle> actual = vehicleReadServiceDatabase.findByRegistration(registration);

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void findByRegistration_NullVehicles_ReturnsEmptyList() {

        final String registration = "HJ89UIK";

        when(vehicleReadDaoMock.getVehicleByFullRegistration(registration)).thenReturn(null);

        List<Vehicle> actual = vehicleReadServiceDatabase.findByRegistration(registration);

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void findByMotTestNumberWithSameRegistrationAndVin_ValidVehicles_ReturnsMappedVehicles() {

        final String registration = "HJ89UIK";
        final String colour = "POMEGRANATE";
        final long motTestNumber = 123456;

        final uk.gov.dvsa.mot.persist.model.Vehicle vehicle = makeTestVehicle(registration, colour);

        final List<uk.gov.dvsa.mot.persist.model.Vehicle> vehiclesList = Arrays.asList(vehicle);

        when(vehicleReadDaoMock.getVehiclesByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(vehiclesList);

        List<Vehicle> actual = vehicleReadServiceDatabase.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0), notNullValue());
        assertThat(actual.get(0).getPrimaryColour(), equalTo(colour));
        assertThat(actual.get(0).getRegistration(), equalTo(registration));
    }

    @Test
    public void findByMotTestNumberWithSameRegistrationAndVin_NoVehicles_ReturnsEmptyList() {

        final long motTestNumber = 123456;

        final List<uk.gov.dvsa.mot.persist.model.Vehicle> vehiclesList = Arrays.asList();

        when(vehicleReadDaoMock.getVehiclesByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(vehiclesList);

        List<Vehicle> actual = vehicleReadServiceDatabase.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void findByMotTestNumberWithSameRegistrationAndVin_NullVehicles_ReturnsEmptyList() {

        final long motTestNumber = 123456;

        when(vehicleReadDaoMock.getVehiclesByMotTestNumberWithSameRegistrationAndVin(motTestNumber)).thenReturn(null);

        List<Vehicle> actual = vehicleReadServiceDatabase.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        assertThat(actual, notNullValue());
        assertThat(actual, isEmpty());
    }

    @Test
    public void getDvlaVehicleByRegistration_ReturnsMappedDvlaVehicle() {

        uk.gov.dvsa.mot.persist.model.DvlaVehicle persistedDvlaVehicle = createPersistedDvlaVehicle();

        when(vehicleReadDaoMock.getDvlaVehicleByRegistration(DEFAULT_REGISTRATION)).thenReturn(persistedDvlaVehicle);

        DvlaVehicle dvlaVehicle = vehicleReadServiceDatabase.getDvlaVehicleByRegistration(DEFAULT_REGISTRATION);

        assertNotNull("Returns an mapped object", dvlaVehicle);
        assertEquals(dvlaVehicle.getRegistration(), DEFAULT_REGISTRATION);
        assertEquals(dvlaVehicle.getColour1(), DEFAULT_COLOR_NAME);
        assertEquals(dvlaVehicle.getColour2(), DEFAULT_COLOR_NAME);
        assertEquals(dvlaVehicle.getFuelType(), DEFAULT_FUEL_TYPE_NAME);
        assertEquals(dvlaVehicle.getBodyTypeCode(), DEFAULT_BODY_TYPE_CODE);
        assertEquals(dvlaVehicle.getModelDetail(), DEFAULT_MODEL_DETAIL_NAME);
        assertEquals(dvlaVehicle.getMakeDetail(), DEFAULT_MAKE_NAME);
    }

    @Test
    public void getDvlaVehicleByRegistration_ReturnsNullWhenNoDvlaVehicleWasFound() {

        final String registration = "ABC123DE";

        when(vehicleReadDaoMock.getDvlaVehicleByRegistration(registration)).thenReturn(null);

        DvlaVehicle dvlaVehicle = vehicleReadServiceDatabase.getDvlaVehicleByRegistration(registration);

        assertNull("No DvlaVehicle found", dvlaVehicle);
    }

    @Test
    public void findDvlaVehicleById_ReturnsListOfMappedDvlaVehicle() {

        uk.gov.dvsa.mot.persist.model.DvlaVehicle persistedDvlaVehicle = createPersistedDvlaVehicle();
        List<uk.gov.dvsa.mot.persist.model.DvlaVehicle> persistedDvlaVehicles = Arrays.asList(persistedDvlaVehicle);

        when(vehicleReadDaoMock.getDvlaVehicleByDvlaVehicleId(DEFAULT_VEHICLE_ID)).thenReturn(persistedDvlaVehicles);

        List<DvlaVehicle> dvlaVehicles = vehicleReadServiceDatabase.findDvlaVehicleById(DEFAULT_VEHICLE_ID);

        assertEquals("Returns a list of mapped dvla vehicles", dvlaVehicles.size(), persistedDvlaVehicles.size());

        DvlaVehicle dvlaVehicle = dvlaVehicles.get(0);
        assertEquals(dvlaVehicle.getRegistration(), DEFAULT_REGISTRATION);
        assertEquals(dvlaVehicle.getColour1(), DEFAULT_COLOR_NAME);
        assertEquals(dvlaVehicle.getColour2(), DEFAULT_COLOR_NAME);
        assertEquals(dvlaVehicle.getFuelType(), DEFAULT_FUEL_TYPE_NAME);
        assertEquals(dvlaVehicle.getBodyTypeCode(), DEFAULT_BODY_TYPE_CODE);
        assertEquals(dvlaVehicle.getModelDetail(), DEFAULT_MODEL_DETAIL_NAME);
        assertEquals(dvlaVehicle.getMakeDetail(), DEFAULT_MAKE_NAME);
    }

    @Test
    public void findDvlaVehicleById_ReturnsEmptyListIfNoDvlaVehiclesWereFound() {

        when(vehicleReadDaoMock.getDvlaVehicleByDvlaVehicleId(DEFAULT_VEHICLE_ID)).thenReturn(null);

        List<DvlaVehicle> dvlaVehicles = vehicleReadServiceDatabase.findDvlaVehicleById(DEFAULT_VEHICLE_ID);

        assertEquals("Empty list is returned",0, dvlaVehicles.size());
    }

    @Test
    public void  findDvlaVehicleByRegistration_ReturnsListOfMappedDvlaVehicles() {

        uk.gov.dvsa.mot.persist.model.DvlaVehicle persistedDvlaVehicle = createPersistedDvlaVehicle();
        List<uk.gov.dvsa.mot.persist.model.DvlaVehicle> persistedDvlaVehicles = Arrays.asList(persistedDvlaVehicle);

        when(vehicleReadDaoMock.getDvlaVehicleByFullRegistration(DEFAULT_REGISTRATION)).thenReturn(persistedDvlaVehicles);

        List<DvlaVehicle> dvlaVehicles = vehicleReadServiceDatabase.findDvlaVehicleByRegistration(DEFAULT_REGISTRATION);

        assertEquals("Returns a list of mapped dvla vehicles", dvlaVehicles.size(), persistedDvlaVehicles.size());

        DvlaVehicle dvlaVehicle = dvlaVehicles.get(0);
        assertEquals(dvlaVehicle.getRegistration(), DEFAULT_REGISTRATION);
        assertEquals(dvlaVehicle.getColour1(), DEFAULT_COLOR_NAME);
        assertEquals(dvlaVehicle.getColour2(), DEFAULT_COLOR_NAME);
        assertEquals(dvlaVehicle.getFuelType(), DEFAULT_FUEL_TYPE_NAME);
        assertEquals(dvlaVehicle.getBodyTypeCode(), DEFAULT_BODY_TYPE_CODE);
        assertEquals(dvlaVehicle.getModelDetail(), DEFAULT_MODEL_DETAIL_NAME);
        assertEquals(dvlaVehicle.getMakeDetail(), DEFAULT_MAKE_NAME);
    }

    @Test
    public void findDvlaVehicleByRegistration_ReturnsEmptyListWhenNoDvlaVehiclesWereFound() {

        when(vehicleReadDaoMock.getDvlaVehicleByFullRegistration(DEFAULT_REGISTRATION)).thenReturn(null);

        List<DvlaVehicle> dvlaVehicles = vehicleReadServiceDatabase.findDvlaVehicleByRegistration(DEFAULT_REGISTRATION);

        assertEquals("Returns a empty list", 0, dvlaVehicles.size());
    }

    @Test
    public void findByDvlaVehicleId_ReturnsEmptyListWhenNoVehiclesWereFound() {

        when(vehicleReadDaoMock.getVehicleByDvlaVehicleId(DEFAULT_VEHICLE_ID)).thenReturn(null);

        List<Vehicle> vehicles = vehicleReadServiceDatabase.findByDvlaVehicleId(DEFAULT_VEHICLE_ID);

        assertEquals("Returns a empty list", 0, vehicles.size());
    }

    private uk.gov.dvsa.mot.persist.model.DvlaVehicle createPersistedDvlaVehicle() {

        final ColourLookup color = new ColourLookup();
        final FuelType fuelType = new FuelType();
        final DvlaModel dvlaModel = new DvlaModel();
        final DvlaMake dvlaMake = new DvlaMake();

        color.setName(DEFAULT_COLOR_NAME);
        fuelType.setName(DEFAULT_FUEL_TYPE_NAME);
        dvlaModel.setName(DEFAULT_MODEL_DETAIL_NAME);
        dvlaMake.setName(DEFAULT_MAKE_NAME);

        uk.gov.dvsa.mot.persist.model.DvlaVehicle persistedDvlaVehicle = new uk.gov.dvsa.mot.persist.model.DvlaVehicle();
        persistedDvlaVehicle.setId(DEFAULT_VEHICLE_ID);
        persistedDvlaVehicle.setRegistration(DEFAULT_REGISTRATION);
        persistedDvlaVehicle.setColour1(color);
        persistedDvlaVehicle.setColour2(color);
        persistedDvlaVehicle.setPropulsion(fuelType);
        persistedDvlaVehicle.setBodyTypeCode(DEFAULT_BODY_TYPE_CODE);
        persistedDvlaVehicle.setModelDetail(dvlaModel);
        persistedDvlaVehicle.setMakeDetail(dvlaMake);

        return persistedDvlaVehicle;
    }
}
