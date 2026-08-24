package ar.edu.itba.pod.concurrency.threadSafety.e1;


import ar.edu.itba.pod.concurrency.service.GenericService;
import ar.edu.itba.pod.concurrency.service.GenericServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for {@link GenericService} using {@link Thread}s
 */
public class GenericServiceConcurrencyTest {
    private static final int VISITS_BY_THREAD = 10_000;
    private static final int THREAD_COUNT = 100;
    private static final int EXPECTED_VISITS = THREAD_COUNT * VISITS_BY_THREAD;

    private GenericService service;

    @BeforeEach
    public final void before() {
        service = new GenericServiceImpl();
    }

    /**
     * Makes VISITS_BY_THREAD visits to the service
     */
    private final Runnable visitor = () -> {
        for (int i = 0; i < VISITS_BY_THREAD; i++) {
            service.addVisit();
        }
    };

    /**
     * generates THREAD_COUNT threads with {@link #visitor} and runs them.
     */
    @Test
    public final void visit_count_with_thread_start() throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(visitor);
            threads[i].start();
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
    }

    /**
     * generates THREAD_COUNT threads with {@link #visitor} and runs them submiting it via
     * the {@link ExecutorService}
     */
    @Test
    public final void visit_count_with_executor_submit() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures.add(pool.submit(visitor));
        }
        for (Future<?> future : futures) {
            future.get();
        }
        assertEquals(EXPECTED_VISITS, service.getVisitCount());
        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
    }
}