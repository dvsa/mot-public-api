package uk.gov.dvsa.mot.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Uncomment @Aspect and @Around for performance logging.
 */
//@Aspect
public class PerfAspect {

    private static final int LOG_METHOD_CALLS_GREATER_THAN_MS = 50;

    @Pointcut("within(uk.gov.dvsa.mot..*)")
    public void withinPublicApi() {
    }

    @Pointcut("execution(public * *(..))")
    public void anyPublicMethod() {
    }

    @Pointcut("execution(private * *(..))")
    public void anyPrivateMethod() {
    }

    @Pointcut("execution(private * getClient(..))")
    public void decryptGetClient() {
    }

    //@Around("withinPublicApi() && (anyPublicMethod() || decryptGetClient())")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Object response;
        long start = System.currentTimeMillis();
        String name = proceedingJoinPoint.getSignature().toLongString();

        try {
            response = proceedingJoinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis() - start;
            if (end > LOG_METHOD_CALLS_GREATER_THAN_MS) {
                System.out.println("The following method " + name + " took " + end + " ms");
            }
        }

        return response;
    }
}
