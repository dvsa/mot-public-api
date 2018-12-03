package uk.gov.dvsa.mot.trade.read.core;

import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger(TradeReadServiceDatabase.class);

    private static final int VEHICLE_PAGE_SIZE = 1000;

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
            uk.gov.dvsa.mot.trade.api.Vehicle vehicle = getDvlaVehicleByRegistration(registration);

            if (vehicle != null) {
                vehicles = Arrays.asList(vehicle);
            }
        }

        return vehicles;
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

    private uk.gov.dvsa.mot.trade.api.Vehicle getDvlaVehicleByRegistration(String registration) {

        DvlaVehicle vehicle = vehicleReadService.getDvlaVehicleByRegistration(registration);

        return createTradeVehicleFromDvlaVehicle(vehicle);
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

    private uk.gov.dvsa.mot.trade.api.Vehicle createTradeVehicleFromDvlaVehicle(DvlaVehicle dvlaVehicle) {

        if (dvlaVehicle == null) {
            return null;
        }

        uk.gov.dvsa.mot.trade.api.Vehicle tradeVehicle = new uk.gov.dvsa.mot.trade.api.Vehicle();

        tradeVehicle.setRegistration(dvlaVehicle.getRegistration());
        tradeVehicle.setMake(dvlaVehicle.getMakeDetail());
        tradeVehicle.setModel(dvlaVehicle.getModelDetail());
        tradeVehicle.setMakeInFull(dvlaVehicle.getMakeInFull());
        tradeVehicle.setPrimaryColour(dvlaVehicle.getColour1());
        tradeVehicle.setFuelType(dvlaVehicle.getFuelType());
        tradeVehicle.setDvlaId(Integer.toString(dvlaVehicle.getDvlaVehicleId()));

        if (!"Not Stated".equalsIgnoreCase(dvlaVehicle.getColour2())) {
            tradeVehicle.setSecondaryColour(dvlaVehicle.getColour2());
        }

        tradeVehicle.setCylinderCapacity(dvlaVehicle.getEngineCapacity());

        if (dvlaVehicle.getFirstRegistrationDate() != null) {
            tradeVehicle.setRegistrationDate(SDF_DATE.format(dvlaVehicle.getFirstRegistrationDate()));
        }

        if (dvlaVehicle.getManufactureDate() != null) {
            tradeVehicle.setManufactureYear(SDF_YEAR.format(dvlaVehicle.getManufactureDate()));
            tradeVehicle.setManufactureDate(SDF_DATE.format(dvlaVehicle.getManufactureDate()));
        }

        Date firstMotDueDate = DvlaVehicleFirstMotDueDateCalculator.calculateFirstMotDueDate(dvlaVehicle);

        if (firstMotDueDate != null) {
            tradeVehicle.setMotTestDueDate(
                    SDF_DATE_ISO_8601.format(firstMotDueDate));
        }

        return tradeVehicle;
    }
}
