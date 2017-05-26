package uk.gov.dvsa.mot.vehicle.read.core ;

import java.util.List ;

import uk.gov.dvsa.mot.vehicle.api.Vehicle ;

public interface VehicleReadService
{
  Vehicle getVehicleById( int id ) ;

  Vehicle getVehicleByIdAndVersion( int it, int version ) ;

  Vehicle getVehicleFromDvlaById( int id ) ;

  Vehicle findByRegistrationAndMake( String registration, String make ) ;

  List<Vehicle> findByRegistration( String registration ) ;

  List<Vehicle> getVehiclesById( int startId, int endId ) ;

  List<Vehicle> getVehiclesByPage( int offset, int limit ) ;

  List<String> getMakes() ;
}