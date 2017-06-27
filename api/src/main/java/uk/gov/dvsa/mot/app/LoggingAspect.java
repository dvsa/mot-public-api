package uk.gov.dvsa.mot.app;


import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.text.MessageFormat;

@Aspect
public class LoggingAspect {

    private static final Logger logger = Logger.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * uk.gov.dvsa.mot.persist.jdbc.*Jdbc.*(..))")
    public void anyPublicJdbcMethod() {

    }

    @Around("anyPublicJdbcMethod()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        if (logger.isTraceEnabled()) {
            String enterMessage = MessageFormat.format("Entering method {0}({1})",
                    MethodSignature.class.cast(proceedingJoinPoint.getSignature()).getMethod().getName(),
                    proceedingJoinPoint.getArgs());

            logger.trace(enterMessage);
        }

        Object response = proceedingJoinPoint.proceed();

        if (logger.isTraceEnabled()) {
            String exitMessage = MessageFormat.format("Exiting method {0}({1}) : {2}",
                    MethodSignature.class.cast(proceedingJoinPoint.getSignature()).getMethod().getName(),
                    proceedingJoinPoint.getArgs(), response);

            logger.trace(exitMessage);
        }

        return response;
    }
}
