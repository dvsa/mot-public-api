package uk.gov.dvsa.mot.trade.api;

public class TradeException extends Exception {
    private static final long serialVersionUID = 1L;

    private final String errorType;
    private final int httpStatus;
    private String requestId;

    protected TradeException(String errorType, int httpStatus, String awsRequestId) {

        this.errorType = errorType;
        this.httpStatus = httpStatus;
        this.requestId = awsRequestId;
    }

    protected TradeException(String errorType, int httpStatus, String message, String requestId) {

        super(message);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
        this.requestId = requestId;
    }

    protected TradeException(String errorType, int httpStatus, Throwable throwable, String requestId) {

        super(throwable);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
        this.requestId = requestId;
    }

    public TradeException(TradeException tradeException, String requestId) {

        super(tradeException);
        this.errorType = tradeException.getErrorType();
        this.httpStatus = tradeException.getHttpStatus();
        this.requestId = requestId;
    }

    public String getErrorType() {

        return errorType;
    }

    public int getHttpStatus() {

        return httpStatus;
    }

    public String getRequestId() {

        return requestId;
    }

    public void setAwsRequestId(String requestId) {

        this.requestId = requestId;
    }

    @Override
    public String getMessage() {

        return "{ \"httpStatus\": \"" + httpStatus + "\", \"errorMessage\": \"" + super.getMessage() + "\", \"awsRequestId\": \"" +
                requestId + "\" }";
    }
}
