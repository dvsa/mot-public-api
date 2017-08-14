package uk.gov.dvsa.mot.persist.jdbc;

@SuppressWarnings({"checkstyle:OperatorWrap", "checkstyle:Indentation"})
class VehicleReadSql {
    static final String queryGetVehicleById = "SELECT `vehicle`.`id` " + ", `vehicle`.`registration` "
            + ", `vehicle`.`registration_collapsed` " + ", `vehicle`.`vin` " + ", `vehicle`.`vin_collapsed` "
            + ", `vehicle`.`model_detail_id` " + ", `vehicle`.`year` " + ", `vehicle`.`manufacture_date` "
            + ", `vehicle`.`first_registration_date` " + ", `vehicle`.`first_used_date` " + ", `vehicle`.`primary_colour_id` "
            + ", `vehicle`.`secondary_colour_id` " + ", `vehicle`.`weight` " + ", `vehicle`.`weight_source_id` "
            + ", `vehicle`.`country_of_registration_id` " + ", `vehicle`.`engine_number` " + ", `vehicle`.`chassis_number` "
            + ", `vehicle`.`is_new_at_first_reg` " + ", `vehicle`.`dvla_vehicle_id` " + ", `vehicle`.`is_damaged` "
            + ", `vehicle`.`is_destroyed` " + ", `vehicle`.`is_incognito` " + ", `vehicle`.`created_by` "
            + ", `vehicle`.`created_on` " + ", `vehicle`.`last_updated_by` " + ", `vehicle`.`last_updated_on` "
            + ", `vehicle`.`version` " + "FROM  `mot2`.`vehicle` " + "WHERE `vehicle`.`id` = ?";

    static final String queryGetMakeById = "SELECT `make`.`id` "
            + ", `make`.`name` "
            + ", `make`.`code` "
            + ", `make`.`is_verified` "
            //      + ", `make`.`is_selectable` "
            + ", `make`.`created_by` "
            + ", `make`.`created_on` "
            + ", `make`.`last_updated_by` "
            + ", `make`.`last_updated_on` "
            + ", `make`.`version` "
            + "FROM  `mot2`.`make` "
            + "WHERE `make`.`id` = ?";

    static final String queryGetModelById = "SELECT `model`.`id` " + ", `model`.`make_id` " + ", `model`.`code` "
            + ", `model`.`name` " + ", `model`.`is_verified` " + ", `model`.`created_by` " + ", `model`.`created_on` "
            + ", `model`.`last_updated_by` " + ", `model`.`last_updated_on` " + ", `model`.`version` "
            + "FROM `mot2`.`model` " + "WHERE `model`.`id` = ?";

    static final String queryGetVehicleClassById = "SELECT `vehicle_class`.`id` " + ", `vehicle_class`.`name` "
            + ", `vehicle_class`.`code` " + ", `vehicle_class`.`vehicle_class_group_id` " + ", `vehicle_class`.`created_by` "
            + ", `vehicle_class`.`created_on` " + ", `vehicle_class`.`last_updated_by` "
            + ", `vehicle_class`.`last_updated_on` " + ", `vehicle_class`.`version` " + "FROM  `mot2`.`vehicle_class` "
            + "WHERE `mot2`.`vehicle_class`.`id` = ?";

    static final String queryGetVehicleClassGroupById = "SELECT `vehicle_class_group`.`id` "
            + ", `vehicle_class_group`.`name` " + ", `vehicle_class_group`.`code` " + ", `vehicle_class_group`.`created_by` "
            + ", `vehicle_class_group`.`created_on` " + ", `vehicle_class_group`.`last_updated_by` "
            + ", `vehicle_class_group`.`last_updated_on` " + ", `vehicle_class_group`.`version` "
            + "FROM `mot2`.`vehicle_class_group` " + "WHERE `vehicle_class_group`.`id` = ?";

    static final String queryGetBodyTypeById = "SELECT `body_type`.`id` " + ", `body_type`.`name` "
            + ", `body_type`.`code` " + ", `body_type`.`display_order` " + ", `body_type`.`created_by` "
            + ", `body_type`.`created_on` " + ", `body_type`.`last_updated_by` " + ", `body_type`.`last_updated_on` "
            + ", `body_type`.`version` " + "FROM  `mot2`.`body_type` " + "WHERE `body_type`.`id` = ?";

    static final String queryGetFuelTypeById = "SELECT `fuel_type`.`id` " + ", `fuel_type`.`name` "
            + ", `fuel_type`.`code` " + ", `fuel_type`.`dvla_propulsion_code` " + ", `fuel_type`.`display_order` "
            + ", `fuel_type`.`created_by` " + ", `fuel_type`.`created_on` " + ", `fuel_type`.`last_updated_by` "
            + ", `fuel_type`.`last_updated_on` " + ", `fuel_type`.`version` " + "FROM `mot2`.`fuel_type` "
            + "WHERE `fuel_type`.`id` = ?";

    static final String queryGetTransmissionTypeById = "SELECT `transmission_type`.`id` "
            + ", `transmission_type`.`name` " + ", `transmission_type`.`code` " + ", `transmission_type`.`display_order` "
            + ", `transmission_type`.`created_by` " + ", `transmission_type`.`created_on` "
            + ", `transmission_type`.`last_updated_by` " + ", `transmission_type`.`last_updated_on` "
            + ", `transmission_type`.`version` " + "FROM `mot2`.`transmission_type` " + "WHERE `transmission_type`.`id` = ?";

    static final String queryGetWheelplanTypeById = "SELECT `wheelplan_type`.`id` " + ", `wheelplan_type`.`code` "
            + ", `wheelplan_type`.`name` " + ", `wheelplan_type`.`description` " + ", `wheelplan_type`.`display_order` "
            + ", `wheelplan_type`.`created_by` " + ", `wheelplan_type`.`created_on` "
            + ", `wheelplan_type`.`last_updated_by` " + ", `wheelplan_type`.`last_updated_on` "
            + ", `wheelplan_type`.`version` " + "FROM `mot2`.`wheelplan_type` " + "WHERE `wheelplan_type`.`id` = ?";

    static final String queryGetModelDetailById = "SELECT `model_detail`.`id` " + ", `model_detail`.`model_id` "
            + ", `model_detail`.`is_verified` " + ", `model_detail`.`vehicle_class_id` " + ", `model_detail`.`body_type_id` "
            + ", `model_detail`.`fuel_type_id` " + ", `model_detail`.`wheelplan_type_id` "
            + ", `model_detail`.`transmission_type_id` " + ", `model_detail`.`eu_classification` "
            + ", `model_detail`.`cylinder_capacity` " + ", `model_detail`.`sha1_concat_ws_chksum` "
            + ", `model_detail`.`created_by` " + ", `model_detail`.`created_on` " + ", `model_detail`.`last_updated_by` "
            + ", `model_detail`.`last_updated_on` " + ", `model_detail`.`version` " + "FROM  `mot2`.`model_detail` "
            + "WHERE `model_detail`.`id` = ?";

    static final String queryGetColourLookupById = "SELECT `colour_lookup`.`id` " + ", `colour_lookup`.`code` "
            + ", `colour_lookup`.`name` " + ", `colour_lookup`.`display_order` " + ", `colour_lookup`.`created_by` "
            + ", `colour_lookup`.`created_on` " + ", `colour_lookup`.`last_updated_by` "
            + ", `colour_lookup`.`last_updated_on` " + ", `colour_lookup`.`version` " + "FROM `mot2`.`colour_lookup` "
            + "WHERE `colour_lookup`.`id` = ?";

    static final String queryGetWeightSourceLookupById = "SELECT `weight_source_lookup`.`id` "
            + ", `weight_source_lookup`.`code` " + ", `weight_source_lookup`.`name` "
            + ", `weight_source_lookup`.`description` " + ", `weight_source_lookup`.`display_order` "
            + ", `weight_source_lookup`.`created_by` " + ", `weight_source_lookup`.`created_on` "
            + ", `weight_source_lookup`.`last_updated_by` " + ", `weight_source_lookup`.`last_updated_on` "
            + ", `weight_source_lookup`.`version` " + "FROM `mot2`.`weight_source_lookup` "
            + "WHERE `weight_source_lookup`.`id` = ?";

    static final String queryGetCountryOfRegistrationLookupById = "SELECT `country_of_registration_lookup`.`id` "
            + ", `country_of_registration_lookup`.`country_lookup_id` " + ", `country_of_registration_lookup`.`name` "
            + ", `country_of_registration_lookup`.`code` " + ", `country_of_registration_lookup`.`licensing_copy` "
            + ", `country_of_registration_lookup`.`created_by` " + ", `country_of_registration_lookup`.`created_on` "
            + ", `country_of_registration_lookup`.`last_updated_by` "
            + ", `country_of_registration_lookup`.`last_updated_on` " + ", `country_of_registration_lookup`.`version` "
            + "FROM `mot2`.`country_of_registration_lookup` " + "WHERE `country_of_registration_lookup`.`id` = ?";

    static final String queryGetCountryLookupById = "SELECT `country_lookup`.`id` " + ", `country_lookup`.`name` "
            + ", `country_lookup`.`code` " + ", `country_lookup`.`iso_code` " + ", `country_lookup`.`display_order` "
            + ", `country_lookup`.`created_by` " + ", `country_lookup`.`created_on` "
            + ", `country_lookup`.`last_updated_by` " + ", `country_lookup`.`last_updated_on` "
            + ", `country_lookup`.`version` " + "FROM `mot2`.`country_lookup` " + "WHERE `country_lookup`.`id` = ?";

    static final String queryGetEmptyReasonMapByVehicle = "SELECT `empty_reason_map`.`id` "
            + ", `empty_reason_map`.`vehicle_id` " + ", `empty_reason_map`.`empty_vin_reason_lookup_id` "
            + ", `empty_reason_map`.`empty_vrm_reason_lookup_id` " + ", `empty_reason_map`.`created_by` "
            + ", `empty_reason_map`.`created_on` " + ", `empty_reason_map`.`last_updated_by` "
            + ", `empty_reason_map`.`last_updated_on` " + ", `empty_reason_map`.`version` "
            + "FROM `mot2`.`empty_reason_map` " + "WHERE `empty_reason_map`.`vehicle_id` = ?";

    static final String queryGetEmptyVinReasonLookupById = "SELECT `empty_vin_reason_lookup`.`id` "
            + ", `empty_vin_reason_lookup`.`code` " + ", `empty_vin_reason_lookup`.`name` "
            + ", `empty_vin_reason_lookup`.`created_by` " + ", `empty_vin_reason_lookup`.`created_on` "
            + ", `empty_vin_reason_lookup`.`last_updated_by` " + ", `empty_vin_reason_lookup`.`last_updated_on` "
            + ", `empty_vin_reason_lookup`.`version` " + "FROM `mot2`.`empty_vin_reason_lookup` "
            + "WHERE `empty_vin_reason_lookup`.`id` = ?";

    static final String queryGetEmptyVrmReasonLookupById = "SELECT `empty_vrm_reason_lookup`.`id` "
            + ", `empty_vrm_reason_lookup`.`code` " + ", `empty_vrm_reason_lookup`.`name` "
            + ", `empty_vrm_reason_lookup`.`created_by` " + ", `empty_vrm_reason_lookup`.`created_on` "
            + ", `empty_vrm_reason_lookup`.`last_updated_by` " + ", `empty_vrm_reason_lookup`.`last_updated_on` "
            + ", `empty_vrm_reason_lookup`.`version` " + "FROM `mot2`.`empty_vrm_reason_lookup` "
            + "WHERE `empty_vrm_reason_lookup`.`id` = ?";

    static final String queryGetMakes = "SELECT `make`.`id` "
            + ", `make`.`name` "
            + ", `make`.`code` "
            + ", `make`.`is_verified` "
            //      + ", `make`.`is_selectable` "
            + ", `make`.`created_by` "
            + ", `make`.`created_on` "
            + ", `make`.`last_updated_by` "
            + ", `make`.`last_updated_on` "
            + ", `make`.`version` "
            + "FROM `mot2`.`make` "
            + "WHERE `make`.`is_verified` = true ";

    static final String queryGetVehiclesById = "SELECT `vehicle`.`id` ";
    static final String queryGetDvlaVehicleById = "SELECT `dvla_vehicle`.`id` ";
    static final String queryGetModelFromDvlaVehicle = "SELECT `dvla_vehicle`.`id` ";

    static final String selectGetVehicle = "SELECT `vehicle`.`id` " + ", `vehicle`.`registration` "
            + ", `vehicle`.`registration_collapsed` " + ", `vehicle`.`vin` " + ", `vehicle`.`vin_collapsed` "
            + ", `vehicle`.`model_detail_id` " + ", `vehicle`.`year` " + ", `vehicle`.`manufacture_date` "
            + ", `vehicle`.`first_registration_date` " + ", `vehicle`.`first_used_date` " + ", `vehicle`.`primary_colour_id` "
            + ", `vehicle`.`secondary_colour_id` " + ", `vehicle`.`weight` " + ", `vehicle`.`weight_source_id` "
            + ", `vehicle`.`country_of_registration_id` " + ", `vehicle`.`engine_number` " + ", `vehicle`.`chassis_number` "
            + ", `vehicle`.`is_new_at_first_reg` " + ", `vehicle`.`dvla_vehicle_id` " + ", `vehicle`.`is_damaged` "
            + ", `vehicle`.`is_destroyed` " + ", `vehicle`.`is_incognito` " + ", `vehicle`.`created_by` "
            + ", `vehicle`.`created_on` " + ", `vehicle`.`last_updated_by` " + ", `vehicle`.`last_updated_on` "
            + ", `vehicle`.`version` " + "FROM  `mot2`.`vehicle` ";

    static final String whereByFullRegistration = "WHERE registration = ? ";

    static final String whereByDvlaVehicleId =
            "JOIN  `mot2`.`model_detail` on `model_detail`.`id` = `vehicle`.`model_detail_id` "
                    + "JOIN  `mot2`.`model` on `model`.`id` = `model_detail`.`model_id` "
                    + "JOIN  `mot2`.`make` on `make`.`id` = `model`.`make_id` "
                    + "JOIN  `mot2`.`dvla_vehicle` on `vehicle`.`id` = `dvla_vehicle`.`vehicle_id` "
                    + "WHERE `dvla_vehicle`.`dvla_vehicle_id` = ? ";

    static final String whereByFullRegAndMake =
            "JOIN  `mot2`.`model_detail` on `model_detail`.`id` = `vehicle`.`model_detail_id` "
                    + "JOIN  `mot2`.`model` on `model`.`id` = `model_detail`.`model_id` "
                    + "JOIN  `mot2`.`make` on `make`.`id` = `model`.`make_id` " + "WHERE `vehicle`.`registration` = ? "
                    + "AND   `make`.`name` LIKE ? ";

    static final String whereByFullRegistrationAndVinEqualVehicleWithMotTestNumber = " JOIN (SELECT `v`.`registration`, `v`.`vin`" +
            "      FROM `mot2`.`vehicle` `v`" +
            "      JOIN `mot2`.`mot_test_current` `c` ON `c`.`vehicle_id` = `v`.`id`" +
            "      WHERE `c`.`number` = ? ) " +
            "      candidate_vehicles ON `mot2`.`vehicle`.`registration` = candidate_vehicles.registration AND" +
            "                             `mot2`.`vehicle`.`vin` = candidate_vehicles.vin";

    static final String queryGetVehicleByFullRegistration = selectGetVehicle + whereByFullRegistration;
    static final String queryGetVehicleByFullRegAndMake = selectGetVehicle + whereByFullRegAndMake;
    static final String queryGetVehiclesByMotTestNumberWithSameRegistrationAndVin =
            selectGetVehicle + whereByFullRegistrationAndVinEqualVehicleWithMotTestNumber;
    static final String queryGetVehicleByDvlaVehicleId = selectGetVehicle + whereByDvlaVehicleId;

    static final String queryGetVehicleByIdAndVersion = "SELECT `vehicle`.`id` " + ", `vehicle`.`registration` "
            + ", `vehicle`.`registration_collapsed` " + ", `vehicle`.`vin` " + ", `vehicle`.`vin_collapsed` "
            + ", `vehicle`.`model_detail_id` " + ", `vehicle`.`year` " + ", `vehicle`.`manufacture_date` "
            + ", `vehicle`.`first_registration_date` " + ", `vehicle`.`first_used_date` " + ", `vehicle`.`primary_colour_id` "
            + ", `vehicle`.`secondary_colour_id` " + ", `vehicle`.`weight` " + ", `vehicle`.`weight_source_id` "
            + ", `vehicle`.`country_of_registration_id` " + ", `vehicle`.`engine_number` " + ", `vehicle`.`chassis_number` "
            + ", `vehicle`.`is_new_at_first_reg` " + ", `vehicle`.`dvla_vehicle_id` " + ", `vehicle`.`is_damaged` "
            + ", `vehicle`.`is_destroyed` " + ", `vehicle`.`is_incognito` " + ", `vehicle`.`created_by` "
            + ", `vehicle`.`created_on` " + ", `vehicle`.`last_updated_by` " + ", `vehicle`.`last_updated_on` "
            + ", `vehicle`.`version` " + "FROM  `mot2`.`vehicle` " + "WHERE `vehicle`.`id` = ? "
            + "AND   `vehicle`.`version` = ? ";

    static final String queryGetVehicleHistByIdAndVersion = "SELECT `vehicle_hist`.`id` "
            + ", `vehicle_hist`.`registration` " + ", `vehicle_hist`.`registration_collapsed` " + ", `vehicle_hist`.`vin` "
            + ", `vehicle_hist`.`vin_collapsed` " + ", `vehicle_hist`.`model_detail_id` " + ", `vehicle_hist`.`year` "
            + ", `vehicle_hist`.`manufacture_date` " + ", `vehicle_hist`.`first_registration_date` "
            + ", `vehicle_hist`.`first_used_date` " + ", `vehicle_hist`.`primary_colour_id` "
            + ", `vehicle_hist`.`secondary_colour_id` " + ", `vehicle_hist`.`weight` "
            + ", `vehicle_hist`.`weight_source_id` " + ", `vehicle_hist`.`country_of_registration_id` "
            + ", `vehicle_hist`.`engine_number` " + ", `vehicle_hist`.`chassis_number` "
            + ", `vehicle_hist`.`is_new_at_first_reg` " + ", `vehicle_hist`.`dvla_vehicle_id` "
            + ", `vehicle_hist`.`is_damaged` " + ", `vehicle_hist`.`is_destroyed` " + ", `vehicle_hist`.`is_incognito` "
            + ", `vehicle_hist`.`created_by` " + ", `vehicle_hist`.`created_on` " + ", `vehicle_hist`.`last_updated_by` "
            + ", `vehicle_hist`.`last_updated_on` " + ", `vehicle_hist`.`version` " + "FROM  `mot2`.`vehicle_hist` "
            + "WHERE `vehicle_hist`.`id` = ? " + "AND   `vehicle_hist`.`version` = ? ";

    static final String queryGetVehiclesByPage = "SELECT `vehicle`.`id` " + ", `vehicle`.`registration` "
            + ", `vehicle`.`registration_collapsed` " + ", `vehicle`.`vin` " + ", `vehicle`.`vin_collapsed` "
            + ", `vehicle`.`model_detail_id` " + ", `vehicle`.`year` " + ", `vehicle`.`manufacture_date` "
            + ", `vehicle`.`first_registration_date` " + ", `vehicle`.`first_used_date` " + ", `vehicle`.`primary_colour_id` "
            + ", `vehicle`.`secondary_colour_id` " + ", `vehicle`.`weight` " + ", `vehicle`.`weight_source_id` "
            + ", `vehicle`.`country_of_registration_id` " + ", `vehicle`.`engine_number` " + ", `vehicle`.`chassis_number` "
            + ", `vehicle`.`is_new_at_first_reg` " + ", `vehicle`.`dvla_vehicle_id` " + ", `vehicle`.`is_damaged` "
            + ", `vehicle`.`is_destroyed` " + ", `vehicle`.`is_incognito` " + ", `vehicle`.`created_by` "
            + ", `vehicle`.`created_on` " + ", `vehicle`.`last_updated_by` " + ", `vehicle`.`last_updated_on` "
            + ", `vehicle`.`version` " + "FROM  `mot2`.`vehicle` " + "WHERE `vehicle`.`id` >= ? "
            + "AND   `vehicle`.`id` < ? ";

    static final String queryGetVehiclesByRegistrationOrVin = "SELECT `colour_lookup`.`id` ";
    static final String queryGetVehiclesByFullRegAndFullVin = "SELECT `colour_lookup`.`id` ";
    static final String queryGetVehiclesByFullRegAndPartialVin = "SELECT `colour_lookup`.`id` ";
    static final String queryGetVehiclesByFullRegAndNullVin = "SELECT `colour_lookup`.`id` ";
    static final String queryGetVehiclesByNullRegAndFullVin = "SELECT `colour_lookup`.`id` ";

}
