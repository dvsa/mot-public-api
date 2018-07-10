package uk.gov.dvsa.mot.vehicle.hgv.validation;

public class TrailerIdFormat {
    public static final String TRAILER_A_REGEX = "\\d{6}[6|7]\\d";
    public static final String TRAILER_B_C_REGEX = "[A|C]\\d{6}";

    public boolean matches(String vrm) {
        return vrm.toUpperCase().matches(TRAILER_A_REGEX)
                || vrm.toUpperCase().matches(TRAILER_B_C_REGEX);
    }
}
