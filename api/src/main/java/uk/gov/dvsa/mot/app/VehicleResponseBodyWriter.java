package uk.gov.dvsa.mot.app;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
@Produces({MediaType.APPLICATION_JSON,
        MediaType.WILDCARD,
        "application/json+v1",
        "application/json+v2",
        "application/json+v3",
        "application/json+v4"})
public class VehicleResponseBodyWriter extends JacksonJaxbJsonProvider {

    @Override
    protected boolean hasMatchingMediaType(MediaType mediaType) {

        return hasMatchingVersionedMediaType(mediaType) || super.hasMatchingMediaType(mediaType);
    }

    private boolean hasMatchingVersionedMediaType(MediaType mediaType) {
        if (mediaType != null) {
            String subtype = mediaType.getSubtype();
            return subtype.startsWith("json");
        }

        return false;
    }
}
