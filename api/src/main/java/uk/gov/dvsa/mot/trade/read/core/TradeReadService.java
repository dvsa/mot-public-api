package uk.gov.dvsa.mot.trade.read.core;

import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.Vehicle;

import java.util.Date;
import java.util.List;

public interface TradeReadService {
    List<String> getMakes();

    //  DisplayMotTestItem getMotTestById( long id ) ;

    //  DisplayMotTestItem getMotTestByNumber( long number ) ;

    List<DisplayMotTestItem> getMotTestsByRegistrationAndMake(String registration, String make);
    //  List<DisplayMotTestItem> getMotTestsByVehicleId( int id ) ;
    //  List<DisplayMotTestItem> getMotTestsByPage( int page ) ;
    //  List<DisplayMotTestItem> getMotTestsByRange( int startVehicleId, int endVehicleId ) ;
    //  List<DisplayMotTestItem> getMotTestsByDatePage( Date date, Integer page ) ;

    List<Vehicle> getVehiclesByVehicleId(int id);

    List<Vehicle> getVehiclesMotTestsByMotTestNumber(long number);

    List<Vehicle> getVehiclesByRegistrationAndMake(String registration, String make);

    List<Vehicle> getVehiclesByPage(int page);

    List<Vehicle> getVehiclesByDatePage(Date date, Integer page);

    Vehicle getLatestMotTestByRegistration(String registration);

    Vehicle getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber);

    //  List<Vehicle> getVehiclesByDatePageTest( Date date, Integer page, Integer pages ) ;
}