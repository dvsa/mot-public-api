package uk.gov.dvsa.mot.persist ;

import java.util.Date ;
import java.util.List ;

import uk.gov.dvsa.mot.trade.api.Vehicle ;

public interface TradeReadDao
{
//  List<DisplayMotTestItem> getMotHistoryByDateRange( Date startDate, Date endDate ) ;
//  List<DisplayMotTestItem> getMotHistoryCurrentByDateRange( Date startDate, Date endDate ) ;
//  List<DisplayMotTestItem> getMotHistoryHistoryByDateRange( Date startDate, Date endDate ) ;
//  List<DisplayMotTestItem> getMotHistoryByRange( int startVehicleId, int endVehicleId ) ;
//  List<DisplayMotTestItem> getMotHistoryCurrentByRange( int startVehicleId, int endVehicleId ) ;
//  List<DisplayMotTestItem> getMotHistoryHistoryByRange( int startVehicleId, int endVehicleId ) ;

  List<Vehicle> getVehiclesMotTestsByVehicleId( int vehicleId ) ;
  List<Vehicle> getVehiclesMotTestsByMotTestNumber( long number ) ;
  List<Vehicle> getVehiclesMotTestsByRegistrationAndMake( String registration, String make ) ;

//  List<Vehicle> getVehiclesMotTestsByDatePage( Date date, int page ) ;
//  List<Vehicle> getVehiclesMotTestsByPage( int page ) ;

  List<Vehicle> getVehiclesMotTestsByDateRange( Date startDate, Date endDate ) ;
  List<Vehicle> getVehiclesMotTestsByRange( int startVehicleId, int endVehicleId ) ;
}