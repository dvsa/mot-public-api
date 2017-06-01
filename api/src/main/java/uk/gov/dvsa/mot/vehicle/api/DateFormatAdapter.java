package uk.gov.dvsa.mot.vehicle.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateFormatAdapter extends XmlAdapter<String, Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public String marshal(Date v) throws Exception {

        return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {

        return dateFormat.parse(v);
    }

}