package uk.gov.dvsa.mot.persist.jdbc;

public class DvlaVehicleReadSql {

    static final String selectDvlaVehicleByRegistration = "" +
            "SELECT `dvla_vehicle`.`id`,  " +
            "  `dvla_vehicle`.`dvla_vehicle_id`,  " +
            "  `dvla_vehicle`.`registration`,  " +
            "  `dvla_model`.`code` `model_code`, " +
            "  `dvla_make`.`code` `make_code`, " +
            "  `model`.`name` `dvsa_model_name`, " +
            "  `make`.`name` `dvsa_make_name`, " +
            "  `dvla_vehicle`.`make_in_full`, " +
            "  `dvla_vehicle`.`colour_1_code`,  " +
            "  `dvla_vehicle`.`colour_2_code`,  " +
            "  `dvla_vehicle`.`manufacture_date`,  " +
            "  `dvla_vehicle`.`first_registration_date`,  " +
            "  `dvla_vehicle`.`eu_classification`,  " +
            "  `dvla_vehicle`.`body_type_code`,  " +
            "  `dvla_vehicle`.`last_updated_on`  " +
            "FROM `mot2`.`dvla_vehicle`  " +
            "  LEFT JOIN `dvla_model` ON `dvla_vehicle`.`model_code` = `dvla_model`.`code` AND `dvla_vehicle`.`make_code` = `dvla_model`" +
            "    .`make_code` " +
            "  LEFT JOIN `dvla_make` ON `dvla_vehicle`.`make_code` = `dvla_make`.`code` " +
            "  LEFT JOIN `dvla_model_model_detail_code_map` `map` on `map`.`dvla_make_code` = `dvla_vehicle`.`make_code` and `map`" +
            "    .`dvla_model_code` = `dvla_vehicle`.`model_code` " +
            "  LEFT JOIN `model` on `map`.`model_id` = `model`.`id` " +
            "  LEFT JOIN `make` on `map`.`make_id` = `make`.`id` and `model`.`make_id` = `make`.`id` " +
            "WHERE `dvla_vehicle`.`registration` = ?";

    static final String selectSingleDvlaVehicleByRegistration = "" +
            "SELECT `dvla_vehicle`.`id`,  " +
            "  `dvla_vehicle`.`dvla_vehicle_id`,  " +
            "  `dvla_vehicle`.`registration`,  " +
            "  `dvla_model`.`code` `model_code`, " +
            "  `dvla_make`.`code` `make_code`, " +
            "  `model`.`name` `dvsa_model_name`, " +
            "  `make`.`name` `dvsa_make_name`, " +
            "  `dvla_vehicle`.`make_in_full`, " +
            "  `dvla_vehicle`.`colour_1_code`,  " +
            "  `dvla_vehicle`.`colour_2_code`,  " +
            "  `dvla_vehicle`.`manufacture_date`,  " +
            "  `dvla_vehicle`.`first_registration_date`,  " +
            "  `dvla_vehicle`.`eu_classification`,  " +
            "  `dvla_vehicle`.`body_type_code`,  " +
            "  `dvla_vehicle`.`last_updated_on`  " +
            "FROM `mot2`.`dvla_vehicle`  " +
            "  LEFT JOIN `dvla_model` ON `dvla_vehicle`.`model_code` = `dvla_model`.`code` AND `dvla_vehicle`.`make_code` = `dvla_model`" +
            "    .`make_code` " +
            "  LEFT JOIN `dvla_make` ON `dvla_vehicle`.`make_code` = `dvla_make`.`code` " +
            "  LEFT JOIN `dvla_model_model_detail_code_map` `map` on `map`.`dvla_make_code` = `dvla_vehicle`.`make_code` and `map`" +
            "    .`dvla_model_code` = `dvla_vehicle`.`model_code` " +
            "  LEFT JOIN `model` on `map`.`model_id` = `model`.`id` " +
            "  LEFT JOIN `make` on `map`.`make_id` = `make`.`id` and `model`.`make_id` = `make`.`id` " +
            "  JOIN `fuel_type` ON `dvla_vehicle`.`propulsion_code` = `fuel_type`.`dvla_propulsion_code` " +
            "WHERE `dvla_vehicle`.`registration` = ? " +
            "  AND `dvla_vehicle`.`vehicle_id` IS NULL " +
            "ORDER BY `dvla_vehicle`.`last_updated_on` DESC " +
            "LIMIT 1";

    static final String selectDvlaVehicleById = "" +
            "SELECT `dvla_vehicle`.`id`,  " +
            "  `dvla_vehicle`.`dvla_vehicle_id`,  " +
            "  `dvla_vehicle`.`registration`,  " +
            "  `dvla_model`.`code` `model_code`, " +
            "  `dvla_make`.`code` `make_code`, " +
            "  `model`.`name` `dvsa_model_name`, " +
            "  `make`.`name` `dvsa_make_name`, " +
            "  `dvla_vehicle`.`make_in_full`, " +
            "  `dvla_vehicle`.`colour_1_code`,  " +
            "  `dvla_vehicle`.`colour_2_code`,  " +
            "  `dvla_vehicle`.`manufacture_date`,  " +
            "  `dvla_vehicle`.`first_registration_date`,  " +
            "  `dvla_vehicle`.`eu_classification`,  " +
            "  `dvla_vehicle`.`body_type_code`,  " +
            "  `dvla_vehicle`.`last_updated_on`  " +
            "FROM `mot2`.`dvla_vehicle`  " +
            "  LEFT JOIN `dvla_model` ON `dvla_vehicle`.`model_code` = `dvla_model`.`code` AND `dvla_vehicle`.`make_code` = `dvla_model`" +
            "    .`make_code` " +
            "  LEFT JOIN `dvla_make` ON `dvla_vehicle`.`make_code` = `dvla_make`.`code` " +
            "  LEFT JOIN `dvla_model_model_detail_code_map` `map` on `map`.`dvla_make_code` = `dvla_vehicle`.`make_code` and `map`" +
            "    .`dvla_model_code` = `dvla_vehicle`.`model_code` " +
            "  LEFT JOIN `model` on `map`.`model_id` = `model`.`id` " +
            "  LEFT JOIN `make` on `map`.`make_id` = `make`.`id` and `model`.`make_id` = `make`.`id` " +
            "WHERE `dvla_vehicle`.`dvla_vehicle_id` = ?";

    static final String selectDvlaModelNameByCode = "SELECT `dvla_model`.`id`, `dvla_model`.`name`, " +
            "`dvla_model`.`code` " +
            "FROM `mot2`.`dvla_model` " +
            "WHERE `dvla_model`.`code` = ? " +
            "AND `dvla_model`.`make_code` = ?";

    static final String selectDvlaMakeNameByCode = "SELECT `dvla_make`.`id`, `dvla_make`.`name`, `dvla_make`.`code` " +
            "FROM `mot2`.`dvla_make` " +
            "WHERE `dvla_make`.`code` = ?";

    static final String selectColourLookupByCode = "SELECT `colour_lookup`.`id`, `colour_lookup`.`name` " +
            "FROM `mot2`.`colour_lookup` " +
            "WHERE `colour_lookup`.`code` = ?";

    static final String selectBodyTypeByCode = "SELECT `body_type`.`id`, `body_type`.`name` " +
            "FROM `mot2`.`body_type` " +
            "WHERE `body_type`.`code` = ?";
}
