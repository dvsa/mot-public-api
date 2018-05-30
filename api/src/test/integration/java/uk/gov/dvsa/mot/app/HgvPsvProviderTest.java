//package uk.gov.dvsa.mot.app;
//
//        import com.amazonaws.serverless.proxy.model.ApiGatewayRequestContext;
//        import com.amazonaws.services.lambda.runtime.Context;
//
//        import org.junit.Before;
//        import org.junit.BeforeClass;
//        import org.junit.Ignore;
//        import org.junit.Test;
//        import org.mockito.Mock;
//
//        import java.io.IOException;
//
//        import javax.ws.rs.container.ContainerRequestContext;
//
//public class HgvPsvProviderTest {
//
//    private static MotrRequestHandler motrRequestHandler;
//    private Context context;
//
//    @Mock
//    private ContainerRequestContext containerRequestContext;
//
//    @Mock
//    private ApiGatewayRequestContext context;
//
//    @BeforeClass
//    public static void setUp() throws IOException {
//        motrRequestHandler = new MotrRequestHandler();
//    }
//
//    @Before
//    public void setAwsRequestAndContext() {
//        context = createContext();
//    }
//
//    @Ignore
//    @Test
//    public void test() throws Exception {
//        motrRequestHandler.getVehicle("EXTODAY");
//    }
//
//    private Context createContext() {
//        return new TestContext();
//    }
//}
