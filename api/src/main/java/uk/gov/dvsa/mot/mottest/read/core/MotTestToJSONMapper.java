package uk.gov.dvsa.mot.mottest.read.core ;

import java.util.List ;

import uk.gov.dvsa.mot.persist.model.MotTestHistory ;
import uk.gov.dvsa.mot.persist.model.MotTestHistoryRfrMap ;
import uk.gov.dvsa.mot.persist.model.MotTestRfrLocationType ;
import uk.gov.dvsa.mot.persist.model.MotTestRfrMap ;
import uk.gov.dvsa.mot.persist.model.ReasonForRejection ;
import uk.gov.dvsa.mot.persist.model.RfrLanguageContentMap ;
import uk.gov.dvsa.mot.persist.model.TiCategoryLanguageContentMap ;

/**
 * Methods to map from an {@link MotTestRfrMap} object to an object suitable for JSON serialisation
 */
class MotTestToJSONMapper
{
  static uk.gov.dvsa.mot.mottest.api.MotTestRfrMap mapMotTestRfrMapSQLtoJSON( MotTestRfrMap storedMotTestRfrMap )
  {
    uk.gov.dvsa.mot.mottest.api.MotTestRfrMap jsonMotTestRfrMap = new uk.gov.dvsa.mot.mottest.api.MotTestRfrMap() ;

    jsonMotTestRfrMap.setType( storedMotTestRfrMap.getReasonForRejectionType().getName() ) ;
    jsonMotTestRfrMap.setLocation( mapMotTestRfrLocationSQLtoJSON( storedMotTestRfrMap.getMotTestRfrLocationType() ) ) ;

    if ( storedMotTestRfrMap.getMotTestRfrMapComment() != null )
    {
      jsonMotTestRfrMap.setComment( storedMotTestRfrMap.getMotTestRfrMapComment().getComment() ) ;
    }

    if ( storedMotTestRfrMap.getMotTestRfrMapCustomDescription() != null )
    {
      jsonMotTestRfrMap
          .setCustomDescription( storedMotTestRfrMap.getMotTestRfrMapCustomDescription().getCustomDescription() ) ;
    }

    if ( storedMotTestRfrMap.getReasonForRejection() != null )
    {
      jsonMotTestRfrMap = mapReasonForRejectionSQLtoJSON( jsonMotTestRfrMap,
          storedMotTestRfrMap.getReasonForRejection() ) ;
    }

    jsonMotTestRfrMap.setFailureDangerous( storedMotTestRfrMap.getFailureDangerous() ) ;
    jsonMotTestRfrMap.setGenerated( storedMotTestRfrMap.getGenerated() ) ;
    jsonMotTestRfrMap.setOnOriginalTest( storedMotTestRfrMap.getOnOriginalTest() ) ;

    return jsonMotTestRfrMap ;
  }

  static uk.gov.dvsa.mot.mottest.api.MotTest mapMotTestHistorySQLtoJSON( MotTestHistory storedMotTest )
  {
    uk.gov.dvsa.mot.mottest.api.MotTest jsonMotTest = new uk.gov.dvsa.mot.mottest.api.MotTest() ;

    jsonMotTest.setId( storedMotTest.getId() ) ;

    jsonMotTest.setVehicleId( storedMotTest.getVehicleId() ) ;
    jsonMotTest.setVehicleVersion( storedMotTest.getVehicleVersion() ) ;

    jsonMotTest.setStatus( storedMotTest.getMotTestStatus().getName() ) ;
    jsonMotTest.setMotTestType( storedMotTest.getMotTestType().getDescription() ) ;

    if ( storedMotTest.getNumber() != null )
    {
      jsonMotTest.setNumber( storedMotTest.getNumber().longValue() ) ;
    }

    jsonMotTest.setStartedDate( storedMotTest.getStartedDate() ) ;
    jsonMotTest.setCompletedDate( storedMotTest.getCompletedDate() ) ;
    jsonMotTest.setIssuedDate( storedMotTest.getIssuedDate() ) ;
    jsonMotTest.setExpiryDate( storedMotTest.getExpiryDate() ) ;

    jsonMotTest.setVehicleWeight( storedMotTest.getVehicleWeight() ) ;
    if ( storedMotTest.getWeightSourceLookup() != null )
    {
      jsonMotTest.setWeightSourceLookup( storedMotTest.getWeightSourceLookup().getName() ) ;
    }

    if ( storedMotTest.getMotTestAddressComment() != null )
    {
      jsonMotTest.setAddressComment( storedMotTest.getMotTestAddressComment().getComment().getComment() ) ;
    }
    if ( storedMotTest.getMotTestComplaintRef() != null )
    {
      jsonMotTest.setComplaintRef( storedMotTest.getMotTestComplaintRef().getComplaintRef() ) ;
    }

    jsonMotTest.setHasRegistration( storedMotTest.getHasRegistration() ) ;
    if ( storedMotTest.getDocument() != null )
    {
      jsonMotTest.setDocumentId( storedMotTest.getDocument().getId() ) ;
    }

    if ( storedMotTest.getMotTestCancelled() != null )
    {
      if ( storedMotTest.getMotTestCancelled().getMotTestReasonForCancelLookup() != null )
      {
        jsonMotTest
            .setReasonForCancel( storedMotTest.getMotTestCancelled().getMotTestReasonForCancelLookup().getReason() ) ;
      }
      if ( storedMotTest.getMotTestCancelled().getComment() != null )
      {
        jsonMotTest.setReasonForCancelComment( storedMotTest.getMotTestCancelled().getComment().getComment() ) ;
      }
    }

    if ( storedMotTest.getMotTestEmergencyReason() != null )
    {
      if ( storedMotTest.getMotTestEmergencyReason().getEmergencyLog() != null )
      {
        jsonMotTest.setEmergencyReasonLogId( storedMotTest.getMotTestEmergencyReason().getEmergencyLog().getId() ) ;
      }
      if ( storedMotTest.getMotTestEmergencyReason().getEmergencyReasonLookup() != null )
      {
        jsonMotTest.setEmergencyReason(
            storedMotTest.getMotTestEmergencyReason().getEmergencyReasonLookup().getDescription() ) ;
      }
      if ( storedMotTest.getMotTestEmergencyReason().getComment() != null )
      {
        jsonMotTest.setEmergencyReason( storedMotTest.getMotTestEmergencyReason().getComment().getComment() ) ;
      }
    }

    jsonMotTest.setOdometerReadingValue( storedMotTest.getOdometerReadingValue() ) ;
    if ( storedMotTest.getOdometerReadingUnit() != null )
    {
      jsonMotTest.setOdometerReadingUnit( storedMotTest.getOdometerReadingUnit() ) ;
    }
    if ( storedMotTest.getOdometerReadingType() != null )
    {
      jsonMotTest.setOdometerReadingType( storedMotTest.getOdometerReadingType() ) ;
    }

    if ( storedMotTest.getMotTestOriginal() != null )
    {
      jsonMotTest.setMotTestIdOriginal( storedMotTest.getMotTestOriginal().getId() ) ;
    }

    if ( storedMotTest.getMotTestPrs() != null )
    {
      jsonMotTest.setPrsMotTestId( storedMotTest.getMotTestPrs().getId() ) ;
    }

    if ( storedMotTest.getPerson() != null )
    {
      jsonMotTest.setPersonId( storedMotTest.getPerson().getId() ) ;
    }

    if ( storedMotTest.getSite() != null )
    {
      jsonMotTest.setSiteId( storedMotTest.getSite().getId() ) ;
    }

    /* need to lookup person */
    jsonMotTest.setCreatedBy( String.valueOf( storedMotTest.getCreatedBy() ) ) ;
    jsonMotTest.setLastUpdatedBy( String.valueOf( storedMotTest.getLastUpdatedBy() ) ) ;
    jsonMotTest.setCreatedOn( storedMotTest.getCreatedOn() ) ;
    jsonMotTest.setLastUpdatedOn( storedMotTest.getLastUpdatedOn() ) ;
    jsonMotTest.setVersion( storedMotTest.getVersion() ) ;

    jsonMotTest.setClientIp( storedMotTest.getClientIp() ) ;

    for ( MotTestHistoryRfrMap storedMotTestRfrMap : storedMotTest.getMotTestHistoryRfrMaps() )
    {
      jsonMotTest.getMotTestRfrMaps().add( mapMotTestHistoryRfrMapSQLtoJSON( storedMotTestRfrMap ) ) ;
    }

    return jsonMotTest ;
  }

  static uk.gov.dvsa.mot.mottest.api.MotTestRfrMap mapMotTestHistoryRfrMapSQLtoJSON(
      MotTestHistoryRfrMap storedMotTestRfrMap )
  {
    uk.gov.dvsa.mot.mottest.api.MotTestRfrMap jsonMotTestRfrMap = new uk.gov.dvsa.mot.mottest.api.MotTestRfrMap() ;

    jsonMotTestRfrMap.setType( storedMotTestRfrMap.getReasonForRejectionType().getName() ) ;
    jsonMotTestRfrMap.setLocation( mapMotTestRfrLocationSQLtoJSON( storedMotTestRfrMap.getMotTestRfrLocationType() ) ) ;

    if ( storedMotTestRfrMap.getMotTestRfrMapComment() != null )
    {
      jsonMotTestRfrMap.setComment( storedMotTestRfrMap.getMotTestRfrMapComment().getComment() ) ;
    }

    if ( storedMotTestRfrMap.getMotTestRfrMapCustomDescription() != null )
    {
      jsonMotTestRfrMap
          .setCustomDescription( storedMotTestRfrMap.getMotTestRfrMapCustomDescription().getCustomDescription() ) ;
    }

    if ( storedMotTestRfrMap.getReasonForRejection() != null )
    {
      jsonMotTestRfrMap = mapReasonForRejectionSQLtoJSON( jsonMotTestRfrMap,
          storedMotTestRfrMap.getReasonForRejection() ) ;
    }

    jsonMotTestRfrMap.setFailureDangerous( storedMotTestRfrMap.getFailureDangerous() ) ;
    jsonMotTestRfrMap.setGenerated( storedMotTestRfrMap.getGenerated() ) ;
    jsonMotTestRfrMap.setOnOriginalTest( storedMotTestRfrMap.getOnOriginalTest() ) ;

    return jsonMotTestRfrMap ;
  }

  static uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation mapMotTestRfrLocationSQLtoJSON(
      MotTestRfrLocationType storedMotTestRfrLocation )
  {
    uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation jsonMotTestRfrLocation = new uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation() ;

    if ( storedMotTestRfrLocation != null )
    {
      jsonMotTestRfrLocation.setLateral( storedMotTestRfrLocation.getLateral() ) ;
      jsonMotTestRfrLocation.setLongitudinal( storedMotTestRfrLocation.getLongitudinal() ) ;
      jsonMotTestRfrLocation.setVertical( storedMotTestRfrLocation.getVertical() ) ;
    }

    return jsonMotTestRfrLocation ;
  }

  static uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation mapMotTestRfrLocationSQLtoJSON( String lateral,
      String longitudinal, String vertical )
  {
    uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation jsonMotTestRfrLocation = new uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation() ;

    jsonMotTestRfrLocation.setLateral( lateral ) ;
    jsonMotTestRfrLocation.setLongitudinal( longitudinal ) ;
    jsonMotTestRfrLocation.setVertical( vertical ) ;

    return jsonMotTestRfrLocation ;
  }

  static uk.gov.dvsa.mot.mottest.api.MotTestRfrMap mapReasonForRejectionSQLtoJSON(
      uk.gov.dvsa.mot.mottest.api.MotTestRfrMap jsonMotTestRfrMap, ReasonForRejection storedReasonForRejection )
  {
    jsonMotTestRfrMap.setInspectionManualReference( storedReasonForRejection.getInspectionManualReference() ) ;

    List<TiCategoryLanguageContentMap> timaps = storedReasonForRejection.getTestItemCategory()
        .getTiCategoryLanguageContentMaps() ;

    for ( TiCategoryLanguageContentMap timap : timaps )
    {
      if ( "EN".equals( timap.getLanguageType().getCode() ) )
      {
        jsonMotTestRfrMap.setTestItemCategory( timap.getDescription() ) ;
      }
    }

    List<RfrLanguageContentMap> rfrlmaps = storedReasonForRejection.getRfrLanguageContentMaps() ;

    for ( RfrLanguageContentMap rfrlmap : rfrlmaps )
    {
      if ( "EN".equals( rfrlmap.getLanguageType().getCode() ) )
      {
        if ( "ADVISORY".equals( jsonMotTestRfrMap.getType() ) )
        {
          jsonMotTestRfrMap.setReasonForRejection( rfrlmap.getAdvisoryText() ) ;
        }
        else
        {
          jsonMotTestRfrMap.setReasonForRejection( rfrlmap.getName() ) ;
        }
      }
    }

    return jsonMotTestRfrMap ;
  }
}
