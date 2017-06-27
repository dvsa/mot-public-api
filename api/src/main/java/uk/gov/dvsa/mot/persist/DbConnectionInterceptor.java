package uk.gov.dvsa.mot.persist;

import com.google.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import uk.gov.dvsa.mot.mottest.read.core.ConnectionManager;

public class DbConnectionInterceptor implements MethodInterceptor {

    private ConnectionManager connectionManager;

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        try {
            connectionManager.openConnection();
            return methodInvocation.proceed();
        } finally {
            connectionManager.closeConnection();
        }
    }
}
