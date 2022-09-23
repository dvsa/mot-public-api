package uk.gov.dvsa.mot.app.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;

public class LoggerParamsManager {
    private static final Logger logger = LogManager.getLogger(LoggerParamsManager.class);

    public static final String AWS_REQUEST_ID_KEY = "AWSRequestId";
    private static final String API_KEY_ID_KEY = "ApiKeyId";
    public static final String URL_PARAMS_KEY = "urlParams";
    private static final String VERSION_KEY = "Version";

    public void resetContext() {
        ThreadContext.clearAll();
    }

    public void populateUrlParamsToLogger(Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            try {
                String json = new ObjectMapper().writeValueAsString(params);
                ThreadContext.put(URL_PARAMS_KEY, json);
            } catch (JsonProcessingException e) {
                logger.warn("Url params cannot be processed to JSON - not saved");
            }
        }
    }

    public void populateRequestIdToLogger(String requestIdKey) {
        if (!isNullOrEmpty(requestIdKey)) {
            ThreadContext.put(LoggerParamsManager.AWS_REQUEST_ID_KEY, requestIdKey);
        }
    }

    public void populateApiKeyIdToLogger(String apiKeyId) {
        if (!isNullOrEmpty(apiKeyId)) {
            ThreadContext.put(API_KEY_ID_KEY, apiKeyId);
        }
    }

    public void populateAcceptVersion(String acceptVersion) {
        if (!isNullOrEmpty(acceptVersion)) {
            ThreadContext.put(VERSION_KEY, acceptVersion);
        }
    }
}
