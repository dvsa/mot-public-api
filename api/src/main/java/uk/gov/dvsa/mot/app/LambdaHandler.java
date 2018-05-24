package uk.gov.dvsa.mot.app;

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Entry point for Lambda
 */
public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final ResourceConfig jerseyApplication = new ResourceConfig()
            .packages("uk.gov.dvsa.mot.app")
            .register(JacksonFeature.class);

    private static final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler =
            JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);


    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {
        context.getLogger().log("Entering lambda handler");
        context.getLogger().log("awsRequestId (correct) : " + context.getAwsRequestId());

        return handler.proxy(request, context);
    }
}