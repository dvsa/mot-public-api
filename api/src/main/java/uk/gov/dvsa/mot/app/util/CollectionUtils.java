package uk.gov.dvsa.mot.app.util;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {

    public static boolean isNullOrEmpty(final Collection<?> collection) {

        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(final Map<?, ?> map) {

        return map == null || map.isEmpty();
    }
}
