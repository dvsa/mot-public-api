package uk.gov.dvsa.mot.app.logging;

import org.apache.logging.log4j.ThreadContext;

import java.util.Map;
import java.util.function.Supplier;

/*
 *  ThreadContextSupplier ensure that the ThreadContext parameters are copied to newly created Thread
 */
public class ThreadContextSupplier<T> implements Supplier<T> {
    private final Supplier<Object> delegate;
    private final Map<String, String> tc;

    public ThreadContextSupplier(Supplier<Object> delegate) {
        this.delegate = delegate;
        this.tc = ThreadContext.getImmutableContext();
    }

    @Override
    public T get() {
        ThreadContext.putAll(tc);
        return (T) delegate.get();
    }
}
