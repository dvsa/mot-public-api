package uk.gov.dvsa.mot.persist;

import uk.gov.dvsa.mot.persist.model.BodyType;
import uk.gov.dvsa.mot.persist.model.ColourLookup;
import uk.gov.dvsa.mot.persist.model.CountryLookup;
import uk.gov.dvsa.mot.persist.model.CountryOfRegistrationLookup;
import uk.gov.dvsa.mot.persist.model.EmptyReasonMap;
import uk.gov.dvsa.mot.persist.model.EmptyVinReasonLookup;
import uk.gov.dvsa.mot.persist.model.EmptyVrmReasonLookup;
import uk.gov.dvsa.mot.persist.model.FuelType;
import uk.gov.dvsa.mot.persist.model.Make;
import uk.gov.dvsa.mot.persist.model.Model;
import uk.gov.dvsa.mot.persist.model.ModelDetail;
import uk.gov.dvsa.mot.persist.model.TransmissionType;
import uk.gov.dvsa.mot.persist.model.Vehicle;
import uk.gov.dvsa.mot.persist.model.VehicleClass;
import uk.gov.dvsa.mot.persist.model.VehicleClassGroup;
import uk.gov.dvsa.mot.persist.model.WeightSourceLookup;
import uk.gov.dvsa.mot.persist.model.WheelplanType;

import java.util.List;

public interface VehicleReadDao {

    Vehicle getVehicleById(int id);

    Vehicle getVehicleByIdAndVersion(int id, int version);

    List<Vehicle> getVehiclesById(int startid, int endid);

    List<Vehicle> getVehiclesByPage(int offset, int limit);

    Vehicle getVehicleByFullRegAndMake(String registration, String make);

    List<Vehicle> getVehicleByFullRegistration(String registration);

    List<Vehicle> getVehiclesByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber);

    List<Make> getMakes();

    ModelDetail getModelDetailById(int id);

    EmptyReasonMap getEmptyReasonMapByVehicle(Vehicle parent);

    EmptyVinReasonLookup getEmptyVinReasonLookupById(int id);

    EmptyVrmReasonLookup getEmptyVrmReasonLookupById(int id);

    Make getMakeById(int id);

    Model getModelById(int id);

    VehicleClass getVehicleClassById(int id);

    VehicleClassGroup getVehicleClassGroupById(int id);

    BodyType getBodyTypeById(int id);

    FuelType getFuelTypeById(int id);

    TransmissionType getTransmissionTypeById(int id);

    WheelplanType getWheelplanTypeById(int id);

    ColourLookup getColourLookupById(int id);

    WeightSourceLookup getWeightSourceLookupById(int id);

    CountryOfRegistrationLookup getCountryOfRegistrationLookupById(int id);

    CountryLookup getCountryLookupById(int id);
}