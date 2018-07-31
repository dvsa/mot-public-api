package uk.gov.dvsa.mot.trade.api.response.mapper;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class VehicleResponseMapperFactoryTests {

    private VehicleResponseMapperFactory vehicleResponseMapperFactory;

    @Before
    public void init() {
        vehicleResponseMapperFactory = new VehicleResponseMapperFactory();
    }

    @Test
    @UseDataProvider("dataProviderForMapperVersions")
    public void getMapper_CorrectlyInstantiatesMapperVersions(String requestedVersion, Class expectedMapper) {
        assertEquals(expectedMapper, vehicleResponseMapperFactory.getMapper(requestedVersion).getClass());
    }

    @DataProvider
    public static Object[][] dataProviderForMapperVersions() {
        return new Object[][]{
                {"v1", VehicleV1ResponseMapper.class},
                {"v2", VehicleV2ResponseMapper.class},
                {"v3", VehicleV3ResponseMapper.class},
                {"v4", VehicleV4ResponseMapper.class},
                {null, VehicleV1ResponseMapper.class}
        };
    }
}
