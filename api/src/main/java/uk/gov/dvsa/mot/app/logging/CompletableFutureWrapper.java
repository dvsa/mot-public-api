package uk.gov.dvsa.mot.app.logging;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/*
 *  CompletableFutureWrapper ensure that the ThreadContext parameters are transmitted between Threads
 */
public class CompletableFutureWrapper {
    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(new ThreadContextSupplier(supplier));
    }
}
