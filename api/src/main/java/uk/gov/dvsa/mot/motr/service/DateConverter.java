package uk.gov.dvsa.mot.motr.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public final class DateConverter {
    private DateConverter() {
    }

    /**
     * Date converter.
     *
     * Interim solution to support dates returned as old java.util.Date, target solution is to use contemporary
     * java.time version throughout the code.
     *
     * @param date date to convert or null
     * @return date or null (if null was passed)
     */
    public static LocalDate toLocalDate(Date date) {
        if (date != null) {
            if (date instanceof java.sql.Date) {
                // java.sql.Date does not support toInstant() method
                return ((java.sql.Date) date).toLocalDate();
            } else {
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        } else {
            return null;
        }
    }

    /**
     * Year extractor.
     *
     * Interim solution, target solution is to use java.time classes.
     *
     * @param date source of year
     * @return year value
     */
    public static Year toYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Year.of(calendar.get(Calendar.YEAR));
    }
}