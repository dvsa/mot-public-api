package uk.gov.dvsa.mot.persist.jdbc;

/* This class should not exists but has been created for optimisation  purposes. It combines the mot_test_x and vehicle
 * tables in a single query which breaks the services model
 */
@SuppressWarnings({"checkstyle:OperatorWrap", "checkstyle:Indentation"})
public class TradeReadSql {
    static final String SELECT_VEHICLES_MOT_TESTS =
            "SELECT `mot_test`.`vehicle_id` as `vehicle_id` "
                    + ", `mot_test`.`id` as `mot_test_id` "
                    + ", `rfrmap`.`id` as `rfrmap_id` "
                    + ", `vehicle`.`registration` "
                    + ", `make`.`name` AS `make_name` "
                    + ", `model`.`name` AS `model_name` "
                    + ", `vehicle`.`first_used_date` "
                    + ", `fuel_type`.`name` AS `fuel_type` "
                    + ", `colour1`.`name` AS `primary_colour` "
                    + ", `colour2`.`name` AS `secondary_colour` "
                    + ", `mot_test`.`started_date` "
                    + ", `mot_test`.`completed_date` as `mot_test_completed_date` "
                    + ", `mot_test_status`.`name` AS `test_result` "
                    + ", `mot_test`.`expiry_date` "
                    + ", `mot_test`.`odometer_value` "
                    + ", `mot_test`.`odometer_unit` "
                    + ", `mot_test`.`odometer_result_type` "
                    + ", `mot_test`.`number` AS `mot_test_number` "
                    + ", `rfr_type`.`name` `rfr_type` "
                    + ", TRIM(CONCAT(TRIM(CONCAT(TRIM(CONCAT(TRIM(CONCAT(TRIM(CONCAT(TRIM(CONCAT(COALESCE(`location`.`lateral`, ''),' ', "
                    + "  COALESCE(`location`.`longitudinal`, ''))), ' ', "
                    + "  COALESCE(`location`.`vertical`, ''))), ' ', "
                    + "  COALESCE(`timap`.`description`, ''))),' ', "
                    + "  CASE WHEN `rfr_type`.`name` = 'ADVISORY' "
                    + "  THEN    COALESCE(`rfrl`.`advisory_text`, '') "
                    + "  ELSE    COALESCE(`rfrl`.`name`, '') "
                    + "  END)),' ',' ', "
                    + "  COALESCE(`rfrmap_comment`.`comment`, ''))), "
                    + "  CASE "
                    + "  WHEN `rfr`.`inspection_manual_reference` IS NOT NULL THEN "
                    + "  CONCAT(' (', `rfr`.`inspection_manual_reference`, ')') ELSE ' ' END)) `rfr_and_comments` ";

    static final String FROM_MOT_TEST_CURRENT = "FROM    `mot2`.`mot_test_current` AS `mot_test` ";
    static final String JOIN_MOT_TEST_CURRENT_RFR_MAP = "LEFT JOIN `mot2`.`mot_test_current_rfr_map` AS `rfrmap` ON `rfrmap`" +
            ".`mot_test_id` = `mot_test`.`id` ";
    static final String FROM_MOT_TEST_HISTORY = "FROM    `mot2`.`mot_test_history` AS `mot_test` ";
    static final String JOIN_MOT_TEST_HISTORY_RFR_MAP = "LEFT JOIN `mot2`.`mot_test_history_rfr_map` AS `rfrmap` ON `rfrmap`" +
            ".`mot_test_id` = `mot_test`.`id` ";

    static final String JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS =
            "LEFT JOIN `mot2`.`mot_test_rfr_location_type` AS `location` ON `location`.`id` = `rfrmap`.`mot_test_rfr_location_type_id` "
                    + "LEFT JOIN `mot2`.`mot_test_rfr_map_comment` AS `rfrmap_comment` ON `rfrmap_comment`.`id` = `rfrmap`.`id` "
                    + "LEFT JOIN `mot2`.`reason_for_rejection_type` AS `rfr_type` ON `rfr_type`.`id` = `rfrmap`.`rfr_type_id` "
                    + "LEFT JOIN `mot2`.`language_type` AS `l` ON `l`.`code` = 'EN' "
                    + "LEFT JOIN `mot2`.`reason_for_rejection` AS `rfr` ON `rfrmap`.`rfr_id` = `rfr`.`id` "
                    + "LEFT JOIN `mot2`.`rfr_language_content_map` AS `rfrl` ON `rfrl`.`rfr_id` = `rfr`.`id` AND `rfrl`" +
                    ".`language_type_id` = `l`.`id` "
                    + "LEFT JOIN `mot2`.`test_item_category` AS `ti` ON `rfr`.`test_item_category_id` = `ti`.`id` "
                    + "LEFT JOIN `mot2`.`ti_category_language_content_map` AS `timap` ON `timap`.`test_item_category_id` = `rfr`" +
                    ".`test_item_category_id` AND `timap`.`language_lookup_id` = `l`.`id` "
                    + "JOIN    `mot2`.`mot_test_type` on `mot_test_type`.`id` = `mot_test`.`mot_test_type_id` "
                    + "JOIN    `mot2`.`mot_test_status` on `mot_test_status`.`id` = `mot_test`.`status_id` "
                    + "JOIN    `mot2`.`vehicle` on `vehicle`.`id` = `mot_test`.`vehicle_id` "
                    + "JOIN    `mot2`.`model_detail` on `model_detail`.`id` = `vehicle`.`model_detail_id` "
                    + "JOIN    `mot2`.`model` on `model`.`id` = `model_detail`.`model_id` "
                    + "JOIN    `mot2`.`make` on `make`.`id` = `model`.`make_id` "
                    + "JOIN    `mot2`.`fuel_type` ON `model_detail`.`fuel_type_id` = `fuel_type`.`id` "
                    + "JOIN    `mot2`.`colour_lookup` AS `colour1` ON `colour1`.`id` = `vehicle`.`primary_colour_id` "
                    + "LEFT JOIN `mot2`.`colour_lookup` AS `colour2` ON `colour2`.`id` = `vehicle`.`secondary_colour_id` ";

    /* standard SQL Keywords */
    static final String WHERE = "WHERE ";
    static final String AND = "AND ";
    static final String UNION_ALL = "UNION ALL ";

    /* standard fixed where clauses */
    static final String MOT_TEST_TYPE_IS_PUBLIC = "`mot_test_type`.`code` IN ('NT','PL', 'PV', 'RT') ";
    static final String MOT_TEST_STATUS_IS_PASS_OR_FAIL = "`mot_test_status`.`code` IN ('P' , 'F') ";
    static final String VEHICLE_REGISTRATION_IS_NOT_INVALID = "`vehicle`.`registration` not like '@%@' ";

    /* standard parameterised where clauses */
    static final String VEHICLE_REGISTRATION_EQUALS = "`vehicle`.`registration` = ? ";
    static final String MAKE_NAME_LIKE = "`make`.`name` LIKE ? ";
    static final String MOT_TEST_VEHICLE_ID_BETWEEN = "`mot_test`.`vehicle_id` >= ? AND `mot_test`.`vehicle_id` < ? ";
    static final String MOT_TEST_NUMBER_EQUALS = "`mot_test`.`number` = ? ";
    static final String MOT_TEST_VEHICLE_ID_EQUALS = "`mot_test`.`vehicle_id` = ? ";

    /* standard orderby clauses */
    static final String ORDER_BY_VEHICLE_COMPLETED_MOT_TEST = "ORDER BY `vehicle_id` ASC, `mot_test_completed_date` DESC, `mot_test_id` " +
            "DESC ";

    static final String QUERY_GET_VEHICLES_MOT_TESTS_BY_REGISTRATION_AND_MAKE =
            SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_CURRENT
                    + JOIN_MOT_TEST_CURRENT_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_EQUALS
                    + AND + MAKE_NAME_LIKE
                    + UNION_ALL
                    + SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_HISTORY
                    + JOIN_MOT_TEST_HISTORY_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_EQUALS
                    + AND + MAKE_NAME_LIKE
                    + ORDER_BY_VEHICLE_COMPLETED_MOT_TEST;

    static final String QUERY_GET_VEHICLES_MOT_TESTS_BY_DATE_RANGE =
            SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_CURRENT
                    + JOIN_MOT_TEST_CURRENT_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_EQUALS
                    + AND + MAKE_NAME_LIKE
                    + UNION_ALL
                    + SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_HISTORY
                    + JOIN_MOT_TEST_HISTORY_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_EQUALS
                    + AND + MAKE_NAME_LIKE
                    + ORDER_BY_VEHICLE_COMPLETED_MOT_TEST;

    static final String QUERY_GET_VEHICLES_MOT_TESTS_BY_RANGE =
            SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_CURRENT
                    + JOIN_MOT_TEST_CURRENT_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + VEHICLE_REGISTRATION_IS_NOT_INVALID
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + MOT_TEST_VEHICLE_ID_BETWEEN
                    + UNION_ALL
                    + SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_HISTORY
                    + JOIN_MOT_TEST_HISTORY_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + VEHICLE_REGISTRATION_IS_NOT_INVALID
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + MOT_TEST_VEHICLE_ID_BETWEEN
                    + ORDER_BY_VEHICLE_COMPLETED_MOT_TEST;

    static final String QUERY_GET_VEHICLES_MOT_TESTS_BY_MOT_TEST_NUMBER =
            SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_CURRENT
                    + JOIN_MOT_TEST_CURRENT_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_IS_NOT_INVALID
                    + AND + MOT_TEST_NUMBER_EQUALS
                    + UNION_ALL
                    + SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_HISTORY
                    + JOIN_MOT_TEST_HISTORY_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_IS_NOT_INVALID
                    + AND + MOT_TEST_NUMBER_EQUALS
                    + ORDER_BY_VEHICLE_COMPLETED_MOT_TEST;

    static final String QUERY_GET_VEHICLES_MOT_TESTS_BY_VEHICLE_ID =
            SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_CURRENT
                    + JOIN_MOT_TEST_CURRENT_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_IS_NOT_INVALID
                    + AND + MOT_TEST_VEHICLE_ID_EQUALS
                    + UNION_ALL
                    + SELECT_VEHICLES_MOT_TESTS
                    + FROM_MOT_TEST_HISTORY
                    + JOIN_MOT_TEST_HISTORY_RFR_MAP
                    + JOIN_MOT_TEST_AND_RFR_MAP_DIMENSIONS
                    + WHERE + MOT_TEST_TYPE_IS_PUBLIC
                    + AND + MOT_TEST_STATUS_IS_PASS_OR_FAIL
                    + AND + VEHICLE_REGISTRATION_IS_NOT_INVALID
                    + AND + MOT_TEST_VEHICLE_ID_EQUALS
                    + ORDER_BY_VEHICLE_COMPLETED_MOT_TEST;

    private TradeReadSql() {

    }

}
