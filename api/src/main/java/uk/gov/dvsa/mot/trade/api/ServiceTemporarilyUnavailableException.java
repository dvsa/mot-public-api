package uk.gov.dvsa.mot.trade.api;

public class ServiceTemporarilyUnavailableException extends TradeException {
    private static final long serialVersionUID = 1L;

    public ServiceTemporarilyUnavailableException(String message, String awsRequestId) {

        super("Service Temporarily Unavailable", 503, message, awsRequestId);
    }
}
