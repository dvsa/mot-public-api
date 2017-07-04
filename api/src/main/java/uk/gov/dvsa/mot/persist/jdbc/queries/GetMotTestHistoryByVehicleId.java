package uk.gov.dvsa.mot.persist.jdbc.queries;

@SuppressWarnings({"checkstyle:OperatorWrap", "checkstyle:Indentation"})
public class GetMotTestHistoryByVehicleId {

    public String buildQuery() {
        return "SELECT `mot_test_history`.`id` "
                + ", `mot_test_history`.`person_id` " + ", `mot_test_history`.`vehicle_id` "
                + ", `mot_test_history`.`vehicle_version` " + ", `mot_test_history`.`document_id` "
                + ", `mot_test_history`.`organisation_id` " + ", `mot_test_history`.`site_id` "
                + ", `mot_test_history`.`has_registration` " + ", `mot_test_history`.`mot_test_type_id` "
                + ", `mot_test_history`.`started_date` " + ", `mot_test_history`.`completed_date` "
                + ", `mot_test_history`.`submitted_date` " + ", `mot_test_history`.`status_id` "
                + ", `mot_test_history`.`issued_date` " + ", `mot_test_history`.`expiry_date` "
                + ", `mot_test_history`.`mot_test_id_original` " + ", `mot_test_history`.`prs_mot_test_id` "
                + ", `mot_test_history`.`number` " + ", `mot_test_history`.`vehicle_weight_source_lookup_id` "
                + ", `mot_test_history`.`vehicle_weight` " + ", `mot_test_history`.`odometer_value` "
                + ", `mot_test_history`.`odometer_unit` " + ", `mot_test_history`.`odometer_result_type` "
                + ", `mot_test_history`.`created_by` " + ", `mot_test_history`.`created_on` "
                + ", `mot_test_history`.`last_updated_by` " + ", `mot_test_history`.`last_updated_on` "
                + ", `mot_test_history`.`version` " + ", `mot_test_history`.`client_ip` " + "FROM  `mot2`.`mot_test_history` "
                + "JOIN  `mot2`.`mot_test_type` on `mot_test_type`.`id` = `mot_test_history`.`mot_test_type_id` "
                + "JOIN  `mot2`.`mot_test_status` on `mot_test_status`.`id` = `mot_test_history`.`status_id` "
                + "WHERE `mot_test_history`.`vehicle_id` = ? " + "AND   `mot_test_type`.`code` IN ( 'NT', 'PL', 'PV', 'RT' ) "
                + "AND   `mot_test_status`.`code` IN ( 'P', 'F' ) "
                + "ORDER BY `mot_test_history`.`completed_date` desc, `mot_test_history`.`id` desc ";
    }
}