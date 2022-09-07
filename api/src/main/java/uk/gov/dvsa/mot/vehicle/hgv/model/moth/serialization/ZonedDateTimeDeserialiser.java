package uk.gov.dvsa.mot.vehicle.hgv.model.moth.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserialiser extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {

        try {
            return toDateTimeFromString(jsonParser.getText());
        } catch (Exception e) {
            return null;
        }
    }

    private ZonedDateTime toDateTimeFromString(String dateAsString) {
        return ZonedDateTime.parse(dateAsString, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC));
    }

}
