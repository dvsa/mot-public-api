package uk.gov.dvsa.mot.mottest.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents the location of an RFR
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"lateral", "longitudinal", "vertical"})
public class MotTestRfrLocation {
    @XmlElement(name = "lateral", required = false)
    private String lateral;
    @XmlElement(name = "longitudinal", required = false)
    private String longitudinal;
    @XmlElement(name = "vertical", required = false)
    private String vertical;

    public String getLateral() {

        return lateral;
    }

    public void setLateral(String lateral) {

        this.lateral = lateral;
    }

    public String getLongitudinal() {

        return longitudinal;
    }

    public void setLongitudinal(String longitudinal) {

        this.longitudinal = longitudinal;
    }

    public String getVertical() {

        return vertical;
    }

    public void setVertical(String vertical) {

        this.vertical = vertical;
    }
}
