package uk.gov.dvsa.mot.trade.api.response.mapper.cvsvehicle;

import uk.gov.dvsa.mot.vehicle.hgv.model.TestHistory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CvsTestTypeMapFilter {

    private static final List<String> visibleTestTypes = new ArrayList<>(
            Arrays.asList(
                    "1ST PAID RETEST",
                    "1ST PG9 FULL INSPECTION & FEE",
                    "1ST PG9 FULL INSPECTION & FEE",
                    "1ST PG9 PAID RETEST",
                    "1ST TEST MV",
                    "1ST TEST TRAILER",
                    "1ST TEST TRI AXLE FREE RETEST",
                    "6A PG9 + S/BELT PAID RETEST",
                    "6A PG9 FULL INSP & FEE + SBELT",
                    "ANNUAL MV",
                    "ANNUAL PSV LARGE",
                    "ANNUAL PSV SMALL",
                    "ANNUAL TEST HAULAGE VEHICLE",
                    "ANNUAL TRAILER",
                    "ARTIC BUS ANNUAL TEST LARGE",
                    "ARTIC BUS FULLY PAID RETEST",
                    "ARTIC BUS PART PAID RETEST",
                    "CLASS 6A ANNUAL",
                    "CLASS 6A FIRST TEST",
                    "CLASS 6A PAID RETEST",
                    "COIF + S/BELT + TEST CERT",
                    "COIF + TEST CERTIFICATE",
                    "COIF PAID RETEST + TEST CERT",
                    "COIF+S/BELT PAID RETEST + CERT",
                    "FIRST TEST HAULAGE VEHICLE",
                    "PAID RETEST",
                    "PAID RETEST HAULAGE VEHICLE",
                    "PART PAID RETEST",
                    "PG9 FULL INSPECTION & FEE",
                    "PG9 PAID RETEST",
                    "PG9 PART PAID RETEST",
                    "TRI AXLE FREE RETEST"
            ));

    public static boolean canDisplayTest(TestHistory testHistory) {
        return visibleTestTypes.contains(testHistory.getTestType());
    }
}
