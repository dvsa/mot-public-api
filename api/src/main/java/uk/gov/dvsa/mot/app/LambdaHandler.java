package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import uk.gov.dvsa.mot.app.logging.LoggerParamsManager;
import uk.gov.dvsa.mot.trade.api.TradeExceptionMapper;

import java.util.Map;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger logger = LogManager.getLogger(LambdaHandler.class);

    private LoggerParamsManager loggerParamsManager = new LoggerParamsManager();

    private static final ResourceConfig jerseyApplication = new ResourceConfig()
            .packages("uk.gov.dvsa.mot.app")
            .register(JacksonFeature.class)
            .register(TradeExceptionMapper.class);

    private static final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler =
            JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {

        Map<String, String> params = request.getPathParameters();
        if (request.getQueryStringParameters() != null && !request.getQueryStringParameters().isEmpty()) {
            params.putAll(request.getQueryStringParameters());
        }

        loggerParamsManager.populateRequestIdToLogger(request.getRequestContext().getRequestId());
        loggerParamsManager.populateApiKeyIdToLogger(request.getRequestContext().getIdentity().getApiKeyId());
        loggerParamsManager.populateUrlParamsToLogger(params);

        logger.info("Entering lambda handler");

        return handler.proxy(request, context);
    }
}
