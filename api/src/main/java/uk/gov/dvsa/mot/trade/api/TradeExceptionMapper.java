package uk.gov.dvsa.mot.trade.api;

import javax.annotation.Priority;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Priority(1)
@Provider
public class TradeExceptionMapper implements ExceptionMapper<TradeException> {

    @Override
    public Response toResponse(TradeException e) {

        return Response
                .status(e.getHttpStatus())
                .type(MediaType.TEXT_PLAIN)
                .entity(e.getMessage())
                .build();
    }
}
