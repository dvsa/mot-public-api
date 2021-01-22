package uk.gov.dvsa.mot.trade.api.response.mapper.searchvehicle;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class SearchVehicleResponseMapperFactoryTest {

    private SearchVehicleResponseMapperFactory factory;

    @Before
    public void init() {
        factory = new SearchVehicleResponseMapperFactory();
    }

    @Test
    @UseDataProvider("dataProviderForMapperVersions")
    public void getMapper_CorrectlyInstantiatesMapperVersions(String requestedVersion, Class expectedMapper) {
        assertEquals(expectedMapper, factory.getMapper(requestedVersion).getClass());
    }

    @DataProvider
    public static Object[][] dataProviderForMapperVersions() {
        return new Object[][]{
                {"v6", SearchVehicleV6ResponseMapper.class},
                {"v7", SearchVehicleV7ResponseMapper.class},
                {"", SearchVehicleV6ResponseMapper.class},
                {null, SearchVehicleV6ResponseMapper.class}
        };
    }
}