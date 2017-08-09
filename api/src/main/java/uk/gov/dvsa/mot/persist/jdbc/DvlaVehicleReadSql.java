package uk.gov.dvsa.mot.persist.jdbc;

public class DvlaVehicleReadSql {

    static final String selectDvlaVehicleByRegistration = "SELECT `dvla_vehicle`.`id`, `dvla_vehicle`.`dvla_vehicle_id`, " +
            "`dvla_vehicle`.`registration`, `dvla_vehicle`.`model_code`, `dvla_vehicle`.`make_code`, `dvla_vehicle`.`colour_1_code`, " +
            "`dvla_vehicle`.`colour_2_code`, `dvla_vehicle`.`manufacture_date`, `dvla_vehicle`.`first_registration_date`, " +
            "`dvla_vehicle`.`eu_classification`, `dvla_vehicle`.`body_type_code`, `dvla_vehicle`.`last_updated_on` " +
            "FROM `mot2`.`dvla_vehicle` " +
            "WHERE `dvla_vehicle`.`registration` = ?";

    static final String selectDvlaModelNameByCode = "SELECT `dvla_model`.`id`, `dvla_model`.`name`, " +
            "`dvla_model`.`code` " +
            "FROM `mot2`.`dvla_model`" +
            "WHERE `dvla_model`.`code` = ?";

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
