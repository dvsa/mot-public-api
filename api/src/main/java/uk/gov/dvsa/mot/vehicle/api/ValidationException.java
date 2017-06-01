package uk.gov.dvsa.mot.vehicle.api;

import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final List<ValidationError> validationErrors;

    public ValidationException(List<ValidationError> validationErrors) {

        this.validationErrors = validationErrors;
    }

    public List<ValidationError> getValidationErrors() {

        return validationErrors;
    }
}
