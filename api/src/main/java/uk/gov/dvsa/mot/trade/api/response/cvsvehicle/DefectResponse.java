package uk.gov.dvsa.mot.trade.api.response.cvsvehicle;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class DefectResponse {

    private String failureItemNo;
    private String failureReason;
    private String severityCode;
    private String severityDescription;

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getFailureItemNo() {
        return failureItemNo;
    }

    public void setFailureItemNo(Integer failureItemNo) {
        if (failureItemNo != null) {
            this.failureItemNo = failureItemNo.toString();
            return;
        }
        this.failureItemNo = null;
    }

    public String getSeverityCode() {
        return severityCode;
    }

    public void setSeverityCode(String severityCode) {
        this.severityCode = severityCode;
    }

    public String getSeverityDescription() {
        return severityDescription;
    }

    public void setSeverityDescription(String severityDescription) {
        this.severityDescription = severityDescription;
    }
}
