package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.net.HttpHeaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import uk.gov.dvsa.mot.app.logging.LoggerParamsManager;
import uk.gov.dvsa.mot.trade.api.TradeExceptionMapper;

import java.util.Map;

import static uk.gov.dvsa.mot.app.util.CollectionUtils.isNullOrEmpty;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private final Logger logger;

    private final LoggerParamsManager loggerParamsManager;

    private final ResourceConfig jerseyApplication;

    private final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public LambdaHandler() {
        configureJulLog4jBridge();
        logger = LogManager.getLogger(LambdaHandler.class);
        loggerParamsManager = new LoggerParamsManager();

        jerseyApplication = new ResourceConfig()
                .packages("uk.gov.dvsa.mot.app")
                .register(JacksonFeature.class)
                .register(TradeExceptionMapper.class);
        handler = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);
        turnOffJerseyAccessLog();
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {
        populateLoggerContextWithParams(request);
        logger.trace("Entering lambda handler");
        return handler.proxy(request, context);
    }

    private void configureJulLog4jBridge() {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }

    private void turnOffJerseyAccessLog() {
        handler.setLogFormatter(null);
    }

    private void populateLoggerContextWithParams(AwsProxyRequest request) {
        loggerParamsManager.resetContext();
        loggerParamsManager.populateRequestIdToLogger(request.getRequestContext().getRequestId());
        loggerParamsManager.populateApiKeyIdToLogger(request.getRequestContext().getIdentity().getApiKeyId());
        loggerParamsManager.populateUrlParamsToLogger(readPathParameters(request));
        loggerParamsManager.populateAcceptVersion(request.getHeaders().get(HttpHeaders.ACCEPT));
    }

    private Map<String, String> readPathParameters(AwsProxyRequest request) {
        Map<String, String> params = null;
        if (!isNullOrEmpty(request.getPathParameters())) {
            params = request.getPathParameters();
        }

        if (params != null && !isNullOrEmpty(request.getQueryStringParameters())) {
            params.putAll(request.getQueryStringParameters());
        }

        return params;
    }
}
