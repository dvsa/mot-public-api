package uk.gov.dvsa.mot.trade.api;

public class InternalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InternalException(Throwable throwable) {

        super(throwable);
    }

    public InternalException(String message) {

        super(message);
    }
}
