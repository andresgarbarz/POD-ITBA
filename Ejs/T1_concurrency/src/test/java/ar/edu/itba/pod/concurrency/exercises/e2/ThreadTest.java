package ar.edu.itba.pod.concurrency.exercises.e2;

import ar.edu.itba.pod.concurrency.exercises.e1.GenericService;
import ar.edu.itba.pod.concurrency.exercises.e1.GenericServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadTest {
    private GenericService service;

    @BeforeEach
    public final void before() {
        service = new GenericServiceImpl();
    }

    @Test
    public void testVisits() throws InterruptedException {
        Thread worker = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                service.addVisit();
            }
        }, "visitor");

        // start() crea un thread nuevo. run() correria en ESTE thread (el del test).
        worker.start();
        worker.join(); // el test espera a que visitor termine

        assertEquals(5, service.getVisitCount());
    }

    @Test
    public void testInterrupt() throws InterruptedException {
        Thread sleeper = new Thread(new SleeperRunnable(20, 200), "sleepy");
        sleeper.start();
        Thread.sleep(50);
        sleeper.interrupt();
        sleeper.join();

        assertEquals(Thread.State.TERMINATED, sleeper.getState());
    }

    @Test
    public void testRandom() throws InterruptedException {
        RandomResultRunnable task = new RandomResultRunnable();
        Thread calculator = new Thread(task, "rng");
        calculator.start();
        calculator.join();

        int value = task.getResult();
        assertTrue(value >= 1 && value <= 100, "result was " + value);
    }
}
