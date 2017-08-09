package uk.gov.dvsa.mot.trade.service;

import uk.gov.dvsa.mot.persist.model.DvlaVehicle;

import java.util.Calendar;
import java.util.Date;

/**
 * This class consists exclusively of static methods that operate on
 * DvlaVehicles and return the calculated due date of their first MOT test.
 *
 * @see DvlaVehicle
 * @see <a href="https://wiki.i-env.net/display/MP/First+MOT+due+date"></a>
 */
public class DvlaVehicleFirstMotDueDateCalculator {

    /**
     * Special body types which affect the calculation of the due date of the
     * first MOT test of vehicles with those body types.
     */
    private static final String TAXI_BODY_TYPE = "07";
    private static final String AMBULANCE_BODY_TYPE = "76";

    /**
     * The EU classification codes used to help calculate the vehicle's first
     * MOT due date.
     */
    private static final String M1 = "M1";
    private static final String M2 = "M2";
    private static final String M3 = "M3";
    private static final String N1 = "N1";
    private static final String L1 = "L1";
    private static final String L2 = "L2";
    private static final String L3 = "L3";
    private static final String L4 = "L4";
    private static final String L5 = "L5";
    private static final String L6 = "L6";

    /**
     * Constants used for manipulating dates and calculating due dates.
     */
    private static final int THREE_YEARS = 3;
    private static final int ONE_YEAR = 1;
    private static final int MINUS_ONE_DAY = -1;

    /**
     * Private constructor as this class provides only static methods and
     * should therefore not be instantiated.
     */
    private DvlaVehicleFirstMotDueDateCalculator() {}

    /**
     * Calculates the due date of a DvlaVehicle's first MOT test.
     *
     * <p>DvlaVehicles are vehicles which have never had an MOT test, therefore
     * their first MOT due date needs to be calculated from the vehicle's date
     * of first registration.
     *
     * @param dvlaVehicle the DvlaVehicle of which to calculate the due date
     *                    of its first MOT, not null
     * @return the Date object representing the due date of the vehicle's first
     * MOT, null if DvlaVehicle with invalid EU classification code is passed
     */
    public static Date calculateFirstMotDueDate(final DvlaVehicle dvlaVehicle) {

        switch (dvlaVehicle.getEuClassification()) {
            case M1:
                return euClassM1(dvlaVehicle);
            case M2:
                return euClassM2(dvlaVehicle);
            case M3:
                return euClassM3(dvlaVehicle);
            case N1:
                return euClassN1(dvlaVehicle);
            case L1:
                return euClassL1ToL6(dvlaVehicle);
            case L2:
                return euClassL1ToL6(dvlaVehicle);
            case L3:
                return euClassL1ToL6(dvlaVehicle);
            case L4:
                return euClassL1ToL6(dvlaVehicle);
            case L5:
                return euClassL1ToL6(dvlaVehicle);
            case L6:
                return euClassL1ToL6(dvlaVehicle);
            default:
                return null; // Return null on invalid EU codes
        }
    }

    /**
     * Calculate first MOT due date for vehicles with EU classification M1.
     *
     * @param dvlaVehicle the DvlaVehicle of which to calculate the due date
     *                    of its first MOT, not null
     * @return the Date object representing the due date of the vehicle's first
     * MOT
     */
    private static Date euClassM1(final DvlaVehicle dvlaVehicle) {
        Date date = dvlaVehicle.getFirstRegistrationDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (dvlaVehicle.getBodyTypeCode() == null) {
            return null;
        }

        if (isDvlaVehicleAmbulanceOrTaxi(dvlaVehicle)) {
            return getDueDateOfNumberOfYearsInFuture(dvlaVehicle, ONE_YEAR);
        } else {
            return getDueDateOfNumberOfYearsInFuture(dvlaVehicle, THREE_YEARS);
        }
    }

    /**
     * Calculate first MOT due date for vehicles with EU classification M2.
     *
     * @param dvlaVehicle the DvlaVehicle of which to calculate the due date
     *                    of its first MOT, not null
     * @return the Date object representing the due date of the vehicle's first
     * MOT
     */
    private static Date euClassM2(final DvlaVehicle dvlaVehicle) {
        return getDueDateOfNumberOfYearsInFuture(dvlaVehicle, ONE_YEAR);
    }

    /**
     * Calculate first MOT due date for vehicles with EU classification M3.
     *
     * @param dvlaVehicle the DvlaVehicle of which to calculate the due date
     *                    of its first MOT, not null
     * @return the Date object representing the due date of the vehicle's first
     * MOT
     */
    private static Date euClassM3(final DvlaVehicle dvlaVehicle) {
        return getDueDateOfNumberOfYearsInFuture(dvlaVehicle, ONE_YEAR);
    }

    /**
     * Calculate first MOT due date for vehicles with EU classification N1.
     *
     * @param dvlaVehicle the DvlaVehicle of which to calculate the due date
     *                    of its first MOT, not null
     * @return the Date object representing the due date of the vehicle's first
     * MOT
     */
    private static Date euClassN1(final DvlaVehicle dvlaVehicle) {
        return getDueDateOfNumberOfYearsInFuture(dvlaVehicle, THREE_YEARS);
    }

    /**
     * Calculate first MOT due date for vehicles with EU classification L1-L6.
     *
     * @param dvlaVehicle the DvlaVehicle of which to calculate the due date
     *                    of its first MOT, not null
     * @return the Date object representing the due date of the vehicle's first
     * MOT
     */
    private static Date euClassL1ToL6(final DvlaVehicle dvlaVehicle) {
        return getDueDateOfNumberOfYearsInFuture(dvlaVehicle, THREE_YEARS);
    }

    /**
     * Checks if the provided vehicle is an ambulance or a taxi. Ambulances and
     * taxis are special body types in the context of MOT due date calculation.
     *
     * @param dvlaVehicle the vehicle of which to check the body type
     * @return if the vehicle is an ambulance or a taxi
     */
    private static boolean isDvlaVehicleAmbulanceOrTaxi(DvlaVehicle dvlaVehicle) {

        return dvlaVehicle.getBodyTypeCode().equals(AMBULANCE_BODY_TYPE)
                || dvlaVehicle.getBodyTypeCode().equals(TAXI_BODY_TYPE);
    }

    /**
     * Manipulates a date by the specified number of years.
     *
     * @param dvlaVehicle the initial date from which the modified date is
     *                    calculated
     * @param years the number of years by which to modify the date
     * @return the first registration date of the vehicle modified by the
     * specified number of years
     */
    private static Date getDueDateOfNumberOfYearsInFuture(final DvlaVehicle dvlaVehicle, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dvlaVehicle.getFirstRegistrationDate());
        calendar.add(Calendar.YEAR, years);
        calendar.add(Calendar.DATE, MINUS_ONE_DAY);

        return calendar.getTime();
    }
}
