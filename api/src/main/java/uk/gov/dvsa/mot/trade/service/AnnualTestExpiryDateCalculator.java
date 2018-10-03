package uk.gov.dvsa.mot.trade.service;

import com.google.common.base.Strings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public class AnnualTestExpiryDateCalculator {
    private static final Logger logger = LogManager.getLogger(AnnualTestExpiryDateCalculator.class);

    private static final String HGV_VEHICLE_TYPE = "HGV";
    private static final String TRAILER_VEHICLE_TYPE = "Trailer";
    private static final DateTimeFormatter HGV_PSV_API_DATE_PATTERN = DateTimeFormatter.ofPattern("d/M/yyyy");

    public Optional<LocalDate> determineAnnualTestExpiryDate(Vehicle vehicle) {
        LocalDate datetime;

        String date = vehicle.getTestCertificateExpiryDate();

        if (Strings.isNullOrEmpty(date)) {
            logger.debug("Vehicle without test certificate expiry date");

            if (vehicle.getVehicleType().equals(TRAILER_VEHICLE_TYPE)) {
                logger.debug("Trailer without test certificate expiry date");
                return Optional.empty();
            }

            String registrationDate = vehicle.getRegistrationDate();

            if (Strings.isNullOrEmpty(registrationDate)) {
                logger.debug("Vehicle without test registration date");
                return Optional.empty();
            }

            datetime = LocalDate.parse(registrationDate, HGV_PSV_API_DATE_PATTERN);

            if (vehicle.getVehicleType().equals(HGV_VEHICLE_TYPE)) {
                logger.debug("Calculating annual test expiry date for HGV Vehicle");
                datetime = datetime.plusMonths(12);
                datetime = datetime.with(TemporalAdjusters.lastDayOfMonth());
            } else {
                logger.debug("Calculating annual test expiry date for PSV Vehicle");
                datetime = datetime.plusMonths(12);
            }
        } else {
            logger.debug("Vehicle with test certificate expiry date found");
            datetime = LocalDate.parse(date, HGV_PSV_API_DATE_PATTERN);
        }

        return Optional.of(datetime);
    }
}
