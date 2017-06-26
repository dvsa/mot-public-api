package uk.gov.dvsa.mot.persist.jdbc.queries;

@SuppressWarnings({"checkstyle:OperatorWrap", "checkstyle:Indentation"})
public class GetMotTestCurrentByVehicleId {

    public String buildQuery(boolean includeAppleals) {
        return "SELECT `mot_test_current`.`id` "
                + ", `mot_test_current`.`person_id` " + ", `mot_test_current`.`vehicle_id` "
                + ", `mot_test_current`.`vehicle_version` " + ", `mot_test_current`.`document_id` "
                + ", `mot_test_current`.`organisation_id` " + ", `mot_test_current`.`site_id` "
                + ", `mot_test_current`.`has_registration` " + ", `mot_test_current`.`mot_test_type_id` "
                + ", `mot_test_current`.`started_date` " + ", `mot_test_current`.`completed_date` "
                + ", `mot_test_current`.`submitted_date` " + ", `mot_test_current`.`status_id` "
                + ", `mot_test_current`.`issued_date` " + ", `mot_test_current`.`expiry_date` "
                + ", `mot_test_current`.`mot_test_id_original` " + ", `mot_test_current`.`prs_mot_test_id` "
                + ", `mot_test_current`.`number` " + ", `mot_test_current`.`vehicle_weight_source_lookup_id` "
                + ", `mot_test_current`.`vehicle_weight` " + ", `mot_test_current`.`odometer_value` "
                + ", `mot_test_current`.`odometer_unit` " + ", `mot_test_current`.`odometer_result_type` "
                + ", `mot_test_current`.`created_by` " + ", `mot_test_current`.`created_on` "
                + ", `mot_test_current`.`last_updated_by` " + ", `mot_test_current`.`last_updated_on` "
                + ", `mot_test_current`.`version` " + ", `mot_test_current`.`client_ip` " + "FROM  `mot2`.`mot_test_current` "
                + "JOIN  `mot2`.`mot_test_type` on `mot_test_type`.`id` = `mot_test_current`.`mot_test_type_id` "
                + "JOIN  `mot2`.`mot_test_status` on `mot_test_status`.`id` = `mot_test_current`.`status_id` "
                + "WHERE `mot_test_current`.`vehicle_id` = ? " + "AND   `mot_test_type`.`code` IN ( 'NT', 'PL', 'PV', 'RT' "
                + (includeAppleals ? ", 'ES', 'EI'" : "") + " ) "
                + "AND   `mot_test_status`.`code` IN ( 'P', 'F' ) "
                + "ORDER BY `mot_test_current`.`completed_date` desc, `mot_test_current`.`id` desc ";
    }
}