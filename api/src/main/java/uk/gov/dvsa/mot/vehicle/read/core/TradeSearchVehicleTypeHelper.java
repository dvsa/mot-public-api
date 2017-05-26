package uk.gov.dvsa.mot.vehicle.read.core ;

public final class TradeSearchVehicleTypeHelper
{
  private static final int PARTIAL_VIN_LENGTH = 6 ;

  private TradeSearchVehicleTypeHelper()
  {
  }

  public static TradeVehicleSearchType getTradeVehicleSearchType( String vin, String registration )
  {
    if ( ( registration != null ) && !( registration.equals( "" ) ) )
    {
      if ( ( vin != null ) && !( vin.equals( "" ) ) )
      {
        return TradeVehicleSearchType.REG_FULL_VIN_NULL ;
      }
      else if ( getVinLengthWithoutSpecialChars( vin ) == PARTIAL_VIN_LENGTH )
      {
        return TradeVehicleSearchType.REG_FULL_VIN_PARTIAL ;
      }
      else
      {
        return TradeVehicleSearchType.REG_FULL_VIN_FULL ;
      }
    }
    else if ( ( vin != null ) && !( vin.equals( "" ) ) )
    {
      return TradeVehicleSearchType.REG_NULL_VIN_FULL ;
    }

    throw new IllegalArgumentException( "Cannot establish search type from parameters provided" ) ;
  }

  // f_collapse function in database strips out these special characters, if we
  // include when checking for a partial vin
  // we will end up with an erroneous search e.g. *****6 becomes WHERE vin LIKE
  // %6
  private static int getVinLengthWithoutSpecialChars( String vin )
  {
    return vin.replaceAll( "[-*/.\\s]", "" ).length() ;
  }
}
