package uk.gov.dvsa.mot.trade.api;

public class BadRequestException extends TradeException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message, String awsRequestId) {

        super("Bad Request", 400, message, awsRequestId);
    }
}
