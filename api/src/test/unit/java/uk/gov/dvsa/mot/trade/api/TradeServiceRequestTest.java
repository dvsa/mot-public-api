package uk.gov.dvsa.mot.trade.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TradeServiceRequestTest {

    TradeServiceRequest tradeServiceRequest;
    private static final String NOT_A_NUMBER = "NOT-A-NUMBER";

    @Before
    public void beforeTest() {
        // Arrange - Create class under test
        tradeServiceRequest = new TradeServiceRequest();
        tradeServiceRequest.setRequestId("123456");
    }

    @Test (expected = BadRequestException.class)
    public void invalidNumberThrowsBadRequestException() throws BadRequestException {

        tradeServiceRequest.getQueryParams().setNumber(NOT_A_NUMBER);
        tradeServiceRequest.getNumber();
    }

    @Test (expected = BadRequestException.class)
    public void invalidPageThrowsBadRequestException() throws BadRequestException {

        tradeServiceRequest.getQueryParams().setPage(NOT_A_NUMBER);
        tradeServiceRequest.getPage();
    }

    @Test (expected = BadRequestException.class)
    public void invalidVehicleIdThrowsBadRequestException() throws BadRequestException {

        tradeServiceRequest.getQueryParams().setVehicleId(NOT_A_NUMBER);
        tradeServiceRequest.getVehicleId();
    }

    @Test (expected = BadRequestException.class)
    public void invalidPathNumberThrowsBadRequestException() throws BadRequestException {

        tradeServiceRequest.getPathParams().setNumber(NOT_A_NUMBER);
        tradeServiceRequest.getPathNumber();
    }


}
