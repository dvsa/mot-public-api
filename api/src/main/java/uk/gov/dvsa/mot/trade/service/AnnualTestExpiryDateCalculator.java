package uk.gov.dvsa.mot.trade.service;

import com.google.common.base.Strings;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.trade.api.BadRequestException;
import uk.gov.dvsa.mot.vehicle.hgv.model.Vehicle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class AnnualTestExpiryDateCalculator {
    private static final Logger logger = Logger.getLogger(AnnualTestExpiryDateCalculator.class);

    private static final String HGV_VEHICLE_TYPE = "HGV";
    private DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("d/M/yyyy");
    private DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public String determineAnnualTestExpiryDate(Vehicle vehicle, String awsRequestId) throws Exception {
        LocalDate datetime;

        String date = vehicle.getTestCertificateExpiryDate();

        if (Strings.isNullOrEmpty(date)) {

            String registrationDate = vehicle.getRegistrationDate();

            if (Strings.isNullOrEmpty(registrationDate)) {
                return null;
            }

            datetime = LocalDate.parse(registrationDate, oldPattern);

            if (vehicle.getVehicleType().equals(HGV_VEHICLE_TYPE)) {
                datetime = datetime.plusMonths(12);
                datetime = datetime.with(TemporalAdjusters.lastDayOfMonth());
            } else {
                datetime = datetime.plusMonths(12);
            }
        } else {
            datetime = LocalDate.parse(date, oldPattern);
        }

        return datetime.format(newPattern);
    }
}
