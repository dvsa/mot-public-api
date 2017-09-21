package uk.gov.dvsa.mot.trade.read.core;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

import uk.gov.dvsa.mot.app.util.CollectionUtils;
import uk.gov.dvsa.mot.mottest.api.MotTest;
import uk.gov.dvsa.mot.mottest.api.MotTestRfrLocation;
import uk.gov.dvsa.mot.mottest.api.MotTestRfrMap;
import uk.gov.dvsa.mot.mottest.read.core.MotTestReadService;
import uk.gov.dvsa.mot.persist.ProvideDbConnection;
import uk.gov.dvsa.mot.persist.TradeReadDao;
import uk.gov.dvsa.mot.trade.api.DisplayMotTestItem;
import uk.gov.dvsa.mot.trade.api.DvlaVehicle;
import uk.gov.dvsa.mot.trade.api.RfrAndAdvisoryItem;
import uk.gov.dvsa.mot.trade.service.DvlaVehicleFirstMotDueDateCalculator;
import uk.gov.dvsa.mot.vehicle.api.Vehicle;
import uk.gov.dvsa.mot.vehicle.read.core.VehicleReadService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Resource
public class TradeReadServiceDatabase implements TradeReadService {
    private static final Logger logger = Logger.getLogger(TradeReadServiceDatabase.class);
    private static final int VEHICLE_PAGE_SIZE = 2000;

    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat SDF_DATE_ISO_8601 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_YEAR = new SimpleDateFormat("yyyy");

    private final MotTestReadService motTestReadService;
    private final VehicleReadService vehicleReadService;
    private final TradeReadDao tradeReadDao;

    @Inject
    public TradeReadServiceDatabase(MotTestReadService motTestReadService, VehicleReadService vehicleReadService,
            TradeReadDao tradeReadDao) {

        this.vehicleReadService = vehicleReadService;
        this.motTestReadService = motTestReadService;
        this.tradeReadDao = tradeReadDao;
    }

    @Override
    @ProvideDbConnection
    public List<String> getMakes() {

        return vehicleReadService.getMakes();
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#
     * getMotTestsByVehicleId1(int)
     */
    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.Vehicle> getVehiclesByVehicleId(int id) {

        return tradeReadDao.getVehiclesMotTestsByVehicleId(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#
     * getMotTestsByVehicleId1(int)
     */
    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.Vehicle> getVehiclesMotTestsByMotTestNumber(long number) {

        return tradeReadDao.getVehiclesMotTestsByMotTestNumber(number);
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#
     * getVehiclesByRegistrationAndMake(String,String)
     */
    @Deprecated
    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.Vehicle> getVehiclesByRegistrationAndMake(String registration, String make) {

        return tradeReadDao.getVehiclesMotTestsByRegistrationAndMake(registration, make);
    }

    /*
 * (non-Javadoc)
 *
 * @see uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#
 * getVehiclesByRegistration(String)
 */
    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.Vehicle> getVehiclesByRegistration(String registration) {

        List<uk.gov.dvsa.mot.trade.api.Vehicle> vehicles = tradeReadDao.getVehiclesMotTestsByRegistration(registration);

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            uk.gov.dvsa.mot.trade.api.Vehicle vehicle = getDvlaVehicleByRegistrationForMoth(registration);

            if (vehicle != null) {
                vehicles = Arrays.asList(vehicle);
            }
        }

        return vehicles;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#
     * getVehiclesByRegistartionAndMake(String,String)
     */
    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.trade.api.Vehicle getLatestMotTestByRegistration(String registration) {

        List<Vehicle> vehicles = vehicleReadService.findByRegistration(registration);

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return getDvlaVehicleByRegistration(registration);
        }

        return getLatestMotTestPassAndMapToTradeVehicle(vehicles);
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.trade.api.Vehicle getDvlaVehicleByRegistration(String registration) {

        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleByRegistration(registration);

        return getLatestDvlaVehicleAndMapToTradeVehicle(vehicles);
    }


    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.trade.api.Vehicle getDvlaVehicleByRegistrationForMoth(String registration) {

        DvlaVehicle vehicle = vehicleReadService.getDvlaVehicleByRegistration(registration);

        return getDvlaVehicleAndMapToTradeVehicle(vehicle);
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.trade.api.Vehicle getDvlaVehicleById(Long dvlaVehicleId) {

        List<DvlaVehicle> vehicles = vehicleReadService.findDvlaVehicleById(dvlaVehicleId);

        return getLatestDvlaVehicleAndMapToTradeVehicle(vehicles);
    }

    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.trade.api.Vehicle getLatestMotTestByDvlaVehicleId(Long dvlaVehicleId) {

        List<Vehicle> vehicles = vehicleReadService.findByDvlaVehicleId(dvlaVehicleId);

        uk.gov.dvsa.mot.trade.api.Vehicle vehicle = getLatestMotTestPassAndMapToTradeVehicle(vehicles);

        if (vehicle == null) {
            vehicle = getDvlaVehicleById(dvlaVehicleId);
        }

        return vehicle;
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#
     * getVehiclesByRegistartionAndMake(String,String)
     */
    @Override
    @ProvideDbConnection
    public uk.gov.dvsa.mot.trade.api.Vehicle getLatestMotTestByMotTestNumberWithSameRegistrationAndVin(Long motTestNumber) {

        List<Vehicle> vehicles = vehicleReadService.findByMotTestNumberWithSameRegistrationAndVin(motTestNumber);

        return getLatestMotTestPassAndMapToTradeVehicle(vehicles);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.gov.dvsa.mot.trade.read.core.TradeReadServiceInterface#getMotTestsByPage
     * (java.lang.Integer, java.lang.Integer)
     */
    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.Vehicle> getVehiclesByPage(int page) {

        int startVehicleId = page * VEHICLE_PAGE_SIZE;
        int endVehicleId = startVehicleId + VEHICLE_PAGE_SIZE;

        return tradeReadDao.getVehiclesMotTestsByRange(startVehicleId, endVehicleId);
    }

    @Override
    @ProvideDbConnection
    public List<uk.gov.dvsa.mot.trade.api.Vehicle> getVehiclesByDatePage(Date date, Integer page) {

        int pages = 1440;
        int minutes = 1440 / pages;

        if (date == null) {
            date = new Date();
        }
        if ((page == null) || (page < 1)) {
            page = 1;
        }
        if (page > pages) {
            page = pages;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.MINUTE, minutes * (page - 1));
        Date startDate = cal.getTime();
        cal.add(Calendar.MINUTE, minutes);
        Date endDate = cal.getTime();

        logger.debug("getMotTestsByDatePage Date " + date + " Page " + page + " Start " + startDate + " End " + endDate);

        return tradeReadDao.getVehiclesMotTestsByDateRange(startDate, endDate);
    }

    @Override
    @ProvideDbConnection
    public List<DisplayMotTestItem> getMotTestsByRegistrationAndMake(String registration, String make) {

        List<DisplayMotTestItem> displayMotTestItems = new ArrayList<>();

        Vehicle vehicle = vehicleReadService.findByRegistrationAndMake(registration, make);

        if (vehicle != null) {
            List<MotTest> motTests = motTestReadService.getMotTestsByVehicleId(vehicle.getId());

            for (MotTest mottest : motTests) {
                DisplayMotTestItem itemToAdd = mapMotTestToDisplayMotTestItem(mottest, vehicle);
                displayMotTestItems.add(itemToAdd);
            }
        }

        return displayMotTestItems;
    }

    private DisplayMotTestItem mapMotTestToDisplayMotTestItem(MotTest motTest, Vehicle vehicle) {

        DisplayMotTestItem displayMotTestItem = new DisplayMotTestItem();
        List<RfrAndAdvisoryItem> rfrAndAdvisoryItemList = new ArrayList<>();

        displayMotTestItem.setMakeName(vehicle.getMake());
        displayMotTestItem.setModelName(vehicle.getModel());
        displayMotTestItem.setFuelType(vehicle.getFuelType());
        if (vehicle.getFirstUsedDate() != null) {
            displayMotTestItem.setFirstUsedDate(SDF_DATE.format(vehicle.getFirstUsedDate()));
        }
        displayMotTestItem.setPrimaryColour(vehicle.getPrimaryColour());
        displayMotTestItem.setRegistration(vehicle.getRegistration());
        if (motTest.getCompletedDate() != null) {
            displayMotTestItem.setCompletedDate(SDF_DATE.format(motTest.getCompletedDate()));
        }
        if (motTest.getExpiryDate() != null) {
            displayMotTestItem.setExpiryDate(SDF_DATE.format(motTest.getExpiryDate()));
        }

        displayMotTestItem.setMotTestNumber(String.valueOf(motTest.getNumber()));
        displayMotTestItem.setTestResult(motTest.getStatus());
        displayMotTestItem.setOdometerValue(String.valueOf(motTest.getOdometerReadingValue()));
        displayMotTestItem.setOdometerUnit(motTest.getOdometerReadingUnit());

        for (MotTestRfrMap rfrmap : motTest.getMotTestRfrMaps()) {
            RfrAndAdvisoryItem rfrAndComment = new RfrAndAdvisoryItem();
            StringBuilder szBuffer = new StringBuilder();

            MotTestRfrLocation location = rfrmap.getLocation();
            if (location.getLateral() != null) {
                szBuffer.append(location.getLateral());
                szBuffer.append(' ');
            }
            if (location.getLongitudinal() != null) {
                szBuffer.append(location.getLongitudinal());
                szBuffer.append(' ');
            }
            if (location.getVertical() != null) {
                szBuffer.append(location.getVertical());
                szBuffer.append(' ');
            }

            if (rfrmap.getTestItemCategory() != null) {
                szBuffer.append(rfrmap.getTestItemCategory());
                szBuffer.append(' ');
            }
            if (rfrmap.getReasonForRejection() != null) {
                szBuffer.append(rfrmap.getReasonForRejection());
                szBuffer.append(' ');
            }
            if (rfrmap.getComment() != null) {
                szBuffer.append(rfrmap.getComment());
            }
            if (rfrmap.getInspectionManualReference() != null) {
                szBuffer.append('(');
                szBuffer.append(rfrmap.getInspectionManualReference());
                szBuffer.append(')');
            }

            rfrAndComment.setText(szBuffer.toString());
            rfrAndComment.setType(rfrmap.getType());
            rfrAndAdvisoryItemList.add(rfrAndComment);
        }

        displayMotTestItem.setRfrAndComments(rfrAndAdvisoryItemList);

        return displayMotTestItem;
    }

    private uk.gov.dvsa.mot.trade.api.Vehicle getDvlaVehicleAndMapToTradeVehicle(DvlaVehicle dvlaVehicle) {

        if (dvlaVehicle == null) {
            return null;
        }

        uk.gov.dvsa.mot.trade.api.Vehicle tradeVehicle = createTradeVehicleOutOfDvlaVehicle(dvlaVehicle);
        tradeVehicle.setFuelType(dvlaVehicle.getFuelType());

        Date firstMotDueDate = DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle);

        if (firstMotDueDate != null) {
            tradeVehicle.setMotTestExpiryDate(
                    SDF_DATE_ISO_8601.format(firstMotDueDate));
        }

        return tradeVehicle;
    }

    private uk.gov.dvsa.mot.trade.api.Vehicle getLatestDvlaVehicleAndMapToTradeVehicle(List<DvlaVehicle> vehicles) {

        if (CollectionUtils.isNullOrEmpty(vehicles)) {
            return null;
        }

        DvlaVehicle dvlaVehicle = selectMostRecentDvlaVehicle(vehicles);
        uk.gov.dvsa.mot.trade.api.Vehicle tradeVehicle = createTradeVehicleOutOfDvlaVehicle(dvlaVehicle);

        if (dvlaVehicle.getEuClassification() == null
                || dvlaVehicle.getEuClassification().equals("N2")
                || dvlaVehicle.getEuClassification().equals("N3")) {
            return null;
        }

        Date firstMotDueDate = DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle);

        if (firstMotDueDate == null) {
            return null;
        }

        tradeVehicle.setMotTestExpiryDate(
                SDF_DATE_ISO_8601.format(firstMotDueDate));

        return tradeVehicle;
    }

    // This should be taken care of in sql query not in code - extracted to method and kept for backward compatibility with MOTR query
    private DvlaVehicle selectMostRecentDvlaVehicle(List<DvlaVehicle> vehicles) {

        DvlaVehicle dvlaVehicle = vehicles.get(0);

        if (vehicles.size() > 1 && dvlaVehicle.getLastUpdatedOn() != null) {
            for (DvlaVehicle dvlaVehicle1 : vehicles) {

                if (dvlaVehicle1.getLastUpdatedOn() != null && dvlaVehicle1.getLastUpdatedOn().after(dvlaVehicle.getLastUpdatedOn())) {
                    dvlaVehicle = dvlaVehicle1;
                }
            }
        }

        return dvlaVehicle;
    }

    private uk.gov.dvsa.mot.trade.api.Vehicle createTradeVehicleOutOfDvlaVehicle(DvlaVehicle dvlaVehicle) {

        uk.gov.dvsa.mot.trade.api.Vehicle tradeVehicle = new uk.gov.dvsa.mot.trade.api.Vehicle();

        tradeVehicle.setRegistration(dvlaVehicle.getRegistration());
        tradeVehicle.setMake(dvlaVehicle.getMakeDetail());
        tradeVehicle.setModel(dvlaVehicle.getModelDetail());
        tradeVehicle.setPrimaryColour(dvlaVehicle.getColour1());
        tradeVehicle.setDvlaId(Long.toString(dvlaVehicle.getDvlaVehicleId()));

        if (!"Not Stated".equalsIgnoreCase(dvlaVehicle.getColour2())) {
            tradeVehicle.setSecondaryColour(dvlaVehicle.getColour2());
        }

        if (dvlaVehicle.getManufactureDate() != null) {
            tradeVehicle.setManufactureYear(SDF_YEAR.format(dvlaVehicle.getManufactureDate()));
        }

        return tradeVehicle;
    }

    private uk.gov.dvsa.mot.trade.api.Vehicle getLatestMotTestPassAndMapToTradeVehicle(List<Vehicle> vehicles) {

        uk.gov.dvsa.mot.trade.api.Vehicle tradeVehicle = null;

        if (!CollectionUtils.isNullOrEmpty(vehicles)) {
            Vehicle vehicle = null;
            MotTest motTest = null;

            for (Vehicle v : vehicles) {
                MotTest mt = motTestReadService.getLatestMotTestPassByVehicle(v);

                // If the vehicle or MOT hasn't been set yet
                // or the latest expiryDate is after the current Expiry date
                if ((vehicle == null || motTest == null || motTest.getExpiryDate() == null)
                        || (mt != null && (mt.getExpiryDate() != null && mt.getExpiryDate().after(motTest.getExpiryDate())))) {
                    vehicle = v;
                    motTest = mt;
                }
            }

            tradeVehicle = new uk.gov.dvsa.mot.trade.api.Vehicle();

            if (vehicle != null) {
                tradeVehicle.setRegistration(vehicle.getRegistration());
                tradeVehicle.setMake(vehicle.getMake());
                tradeVehicle.setModel(vehicle.getModel());
                tradeVehicle.setPrimaryColour(vehicle.getPrimaryColour());

                if (!"Not Stated".equalsIgnoreCase(vehicle.getSecondaryColour())) {
                    tradeVehicle.setSecondaryColour(vehicle.getSecondaryColour());
                }

                if (vehicle.getManufactureDate() != null) {
                    tradeVehicle.setManufactureYear(SDF_YEAR.format(vehicle.getManufactureDate()));
                }
            }

            if (motTest != null) {
                if (motTest.getExpiryDate() != null) {
                    tradeVehicle.setMotTestExpiryDate(SDF_DATE_ISO_8601.format(motTest.getExpiryDate()));
                }
                if (motTest.getNumber() != null) {
                    tradeVehicle.setMotTestNumber(motTest.getNumber().toString());
                }
            }
        }

        return tradeVehicle;
    }
}
