package uk.gov.dvsa.mot.trade.api;

public class InvalidResourceException extends TradeException {
    private static final long serialVersionUID = 1L;

    public InvalidResourceException(String message) {

        super("Invalid Resource", 404, message, null);
    }

    public InvalidResourceException(String message, String awsRequestId) {

        super("Invalid Resource", 404, message, awsRequestId);
    }

    public InvalidResourceException(Throwable throwable, String awsRequestId) {

        super("Invalid Resource", 404, throwable, awsRequestId);
    }
}
