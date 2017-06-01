package uk.gov.dvsa.mot.test.utility;

import java.util.HashMap;
import java.util.Map;

public class TradeExceptionTypes {
    private static Map<Integer, String> types;

    public static String get(Integer httpCode) {

        if (types == null) {
            initializeTypes();
        }

        return types.get(httpCode);
    }

    private static void initializeTypes() {

        types = new HashMap<>();
        types.put(500, "Internal Server Error");
        types.put(400, "Bad Request");
        types.put(401, "Resource Not Found");
    }
}
