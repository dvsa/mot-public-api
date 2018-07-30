package uk.gov.dvsa.mot.app.logging;

import org.apache.logging.log4j.ThreadContext;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;

public class CompleteFutureWrapperTest {
    public String value = "value";

    @Test
    public void supplyAsync_shouldCopyImmutableThreadContextToChildThread() throws Exception {
        CompletableFuture<Boolean> answer;
        ThreadContext.put(LoggerParamsManager.AWSRequestIdKey, value);

        answer = CompletableFutureWrapper.supplyAsync(() -> isChildThreadImmutableContextDuplicated(value));

        assertTrue(answer.get());
    }

    private Boolean isChildThreadImmutableContextDuplicated(String expectedValue) {
        String value = ThreadContext.get(LoggerParamsManager.AWSRequestIdKey);

        return expectedValue.equals(value);
    }
}
