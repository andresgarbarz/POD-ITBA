package ar.edu.itba.pod.concurrency.exercises.e3;

import ar.edu.itba.pod.concurrency.exercises.e1.GenericService;
import ar.edu.itba.pod.concurrency.exercises.e1.GenericServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExecutorTest {
    private GenericService service;

    @BeforeEach
    public final void before() {
        service = new GenericServiceImpl();
    }

    @Test
    public void testVisits() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            Future<Integer> future = pool.submit(() -> {
                for (int i = 0; i < 5; i++) {
                    service.addVisit();
                }
                return service.getVisitCount();
            });
            assertEquals(5, future.get());
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow();
            }
        }
    }
}
