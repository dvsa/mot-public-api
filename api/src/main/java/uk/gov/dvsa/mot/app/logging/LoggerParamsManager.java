package uk.gov.dvsa.mot.app.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.Map;

public class LoggerParamsManager {
    private static final Logger logger = LogManager.getLogger(LoggerParamsManager.class);
    public static final String AWSRequestIdKey = "AWSRequestId";
    public static final String ApiKeyId = "ApiKeyId";
    public static final String UrlParamsKey = "urlParams";

    public void populateUrlParamsToLogger(Map<String, String> params) {
        if (!params.isEmpty()) {
            try {
                String json = new ObjectMapper().writeValueAsString(params);
                ThreadContext.put(UrlParamsKey, json);
            } catch (JsonProcessingException e) {
                logger.error("Url params cannot be processed to JSON");
                logger.warn("Url params not saved");
            }
        }
    }

    public void populateRequestIdToLogger(String requestIdKey) {
        if (requestIdKey != null && !requestIdKey.isEmpty()) {
            ThreadContext.put(LoggerParamsManager.AWSRequestIdKey, requestIdKey);
        }
    }

    public void populateApiKeyIdToLogger(String apiKeyId) {
        if (apiKeyId != null && !apiKeyId.isEmpty()) {
            ThreadContext.put(ApiKeyId, apiKeyId);
        }
    }
}
