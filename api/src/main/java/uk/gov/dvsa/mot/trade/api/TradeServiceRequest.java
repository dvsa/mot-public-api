package uk.gov.dvsa.mot.trade.api;

import java.util.Arrays;

public class TradeServiceRequest {
    private String method;
    private Header[] header;
    private MotTestQueryParams queryParams = new MotTestQueryParams();
    private MotTestPathParams pathParams = new MotTestPathParams();

    @Override
    public String toString() {

        return "TradeServiceRequest{" +
                "method='" + method + '\'' +
                ", header=" + Arrays.toString(header) +
                ", queryParams=" + queryParams +
                ", pathParams=" + pathParams +
                '}';
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

    public Long getNumber() {

        return queryParams.number;
    }

    public void setNumber(Long number) {

        queryParams.number = number;
    }

    public Integer getVehicleId() {

        return queryParams.vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {

        queryParams.vehicleId = vehicleId;
    }

    public String getRegistration() {

        return queryParams.registration;
    }

    public void setRegistration(String registration) {

        queryParams.registration = registration;
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

    public Integer getPage() {

        return queryParams.page;
    }

    public void setPage(Integer page) {

        queryParams.page = page;
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
        private Long number;
        private Integer vehicleId;
        private String registration;
        private String make;
        private String date;
        private Integer page;
        private Integer pages;

        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

        public Long getNumber() {

            return number;
        }

        public void setNumber(Long number) {

            this.number = number;
        }

        public Integer getVehicleId() {

            return vehicleId;
        }

        public void setVehicleId(Integer vehicleId) {

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

        public Integer getPage() {

            return page;
        }

        public void setPage(Integer page) {

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
        private Integer dvlaVehicleId;
        private Long number;
        private String registration;
        private String make;

        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

        public Integer getDvlaVehicleId() {

            return dvlaVehicleId;
        }

        public void setDvlaVehicleId(Integer dvlaVehicleId) {

            this.dvlaVehicleId = dvlaVehicleId;
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

        public Long getNumber() {

            return number;
        }

        public void setNumber(Long number) {

            this.number = number;
        }

        @Override
        public String toString() {

            return "MotTestPathParams{" +
                    "id=" + id +
                    ", dvlaVehicleId=" + dvlaVehicleId +
                    ", number=" + number +
                    ", registration='" + registration + '\'' +
                    ", make='" + make + '\'' +
                    '}';
        }
    }
}

