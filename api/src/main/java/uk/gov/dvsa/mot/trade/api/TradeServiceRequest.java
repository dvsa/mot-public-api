package uk.gov.dvsa.mot.trade.api;

import com.google.common.base.Strings;

import java.util.Arrays;

public class TradeServiceRequest {
    private String method;
    private Header[] header;
    private MotTestQueryParams queryParams = new MotTestQueryParams();
    private MotTestPathParams pathParams = new MotTestPathParams();
    private AnnualTestQueryParams annualTestQueryParams = new AnnualTestQueryParams();

    private String requestId;

    @Override
    public String toString() {

        return "TradeServiceRequest{" +
                "method='" + method + '\'' +
                ", header=" + Arrays.toString(header) +
                ", queryParams=" + queryParams +
                ", pathParams=" + pathParams +
                ", annualTestQueryParams=" + annualTestQueryParams +
                '}';
    }

    public Long confirmValidLong(String longString, String message) throws BadRequestException {

        if (Strings.isNullOrEmpty(longString)) {
            return null;
        }
        try {
            return Long.parseLong(longString);
        } catch (NumberFormatException e) {
            throw new BadRequestException(message + ": " + longString, requestId);
        }
    }

    public Integer confirmValidInteger(String integerString, String message) throws BadRequestException {

        if (Strings.isNullOrEmpty(integerString)) {
            return null;
        }
        try {
            return Integer.parseInt(integerString);
        } catch (NumberFormatException e) {
            throw new BadRequestException(message + ": " + integerString, requestId);
        }
    }

    public void setRequestId(String requestId) {

        this.requestId = requestId;
    }


    public String getMethod() {

        return method;
    }

    public void setMethod(String method) {

        this.method = method;
    }

    public Header[] getHeader() {

        return header;
    }

    public void setHeader(Header[] header) {

        this.header = header;
    }

    public AnnualTestQueryParams getAnnualTestQueryParams() {

        return annualTestQueryParams;
    }

    public void setAnnualTestQueryParams(AnnualTestQueryParams annualTestQueryParams) {

        this.annualTestQueryParams = annualTestQueryParams;
    }

    public MotTestQueryParams getQueryParams() {

        return queryParams;
    }

    public void setQueryParams(MotTestQueryParams queryParams) {

        this.queryParams = queryParams;
    }

    public MotTestPathParams getPathParams() {

        return pathParams;
    }

    public void setPathParams(MotTestPathParams pathParams) {

        this.pathParams = pathParams;
    }

    public Long getId() {

        return queryParams.id;
    }

    public void setId(Long id) {

        queryParams.id = id;
    }

    public Long getNumber() throws BadRequestException {

        return confirmValidLong(queryParams.number, "Invalid MOT number");

    }

    public Long getPathNumber() throws BadRequestException {

        return confirmValidLong(pathParams.number, "Invalid MOT number");

    }

    public void setNumber(Long number) {

        queryParams.number = number.toString();
    }

    public Integer getVehicleId() throws BadRequestException {

        return confirmValidInteger(queryParams.vehicleId, "Invalid vehicle id");
    }

    public void setVehicleId(Integer vehicleId) {

        queryParams.vehicleId = vehicleId.toString();
    }

    public String getRegistration() {

        return queryParams.registration;
    }

    public void setRegistration(String registration) {

        queryParams.registration = registration;
    }

    public String getRegistrations() {

        return annualTestQueryParams.registrations;
    }

    public void setRegistrations(String registrations) {

        annualTestQueryParams.registrations = registrations;
    }

    public String getMake() {

        return queryParams.make;
    }

    public void setMake(String make) {

        queryParams.make = make;
    }

    public String getDate() {

        return queryParams.date;
    }

    public void setDate(String date) {

        queryParams.date = date;
    }

    public Integer getPage() throws BadRequestException {

        return confirmValidInteger(queryParams.page, "Invalid page");
    }

    public void setPage(Integer page) {

        queryParams.page = page.toString();
    }

    public Integer getPages() {

        return queryParams.pages;
    }

    public void setPages(Integer pages) {

        queryParams.pages = pages;
    }

    public class Header {
        private String param;
        private String value;

        public String getParam() {

            return param;
        }

        public void setParam(String param) {

            this.param = param;
        }

        public String getValue() {

            return value;
        }

        public void setValue(String value) {

            this.value = value;
        }
    }

    public class MotTestQueryParams {

        private Long id;
        private String number;
        private String vehicleId;
        private String registration;
        private String make;
        private String date;
        private String page;
        private Integer pages;

        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

        public String getNumber() {

            return number;
        }

        public void setNumber(String number) {

            this.number = number;
        }

        public String getVehicleId() {

            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {

            this.vehicleId = vehicleId;
        }

        public String getRegistration() {

            return registration;
        }

        public void setRegistration(String registration) {

            this.registration = registration;
        }

        public String getMake() {

            return make;
        }

        public void setMake(String make) {

            this.make = make;
        }

        public String getDate() {

            return date;
        }

        public void setDate(String date) {

            this.date = date;
        }

        public String getPage() {

            return page;
        }

        public void setPage(String page) {

            this.page = page;
        }

        public Integer getPages() {

            return pages;
        }

        public void setPages(Integer pages) {

            this.pages = pages;
        }

        @Override
        public String toString() {

            return "MotTestQueryParams{" +
                    "id=" + id +
                    ", number=" + number +
                    ", vehicleId=" + vehicleId +
                    ", registration='" + registration + '\'' +
                    ", make='" + make + '\'' +
                    ", date='" + date + '\'' +
                    ", page=" + page +
                    ", pages=" + pages +
                    '}';
        }
    }

    public class MotTestPathParams {
        private Long id;
        private Integer dvlaId;
        private String number;
        private String registration;
        private String make;

        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

        public Integer getDvlaId() {

            return dvlaId;
        }

        public void setDvlaId(Integer dvlaId) {

            this.dvlaId = dvlaId;
        }

        public String getRegistration() {

            return registration;
        }

        public void setRegistration(String registration) {

            this.registration = registration;
        }

        public String getMake() {

            return make;
        }

        public void setMake(String make) {

            this.make = make;
        }

        public String getNumber() {

            return number;
        }

        public void setNumber(String number) {

            this.number = number;
        }

        @Override
        public String toString() {

            return "MotTestPathParams{" +
                    "id=" + id +
                    ", dvlaId=" + dvlaId +
                    ", number=" + number +
                    ", registration='" + registration + '\'' +
                    ", make='" + make + '\'' +
                    '}';
        }
    }

    public class AnnualTestQueryParams {

        private String registrations;

        public String getRegistrations() {
            return registrations;
        }

        public void setRegistrations(String registrations) {
            this.registrations = registrations;
        }

        @Override
        public String toString() {

            return "AnnualTestQueryParams{" +
                    "registrations='" + registrations + '\'' +
                    '}';
        }
    }
}

