package ar.edu.itba.pod.concurrency.exercises.e2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * El resultado vive en el Runnable. El Thread solo ejecuta run().
 */
public class RandomResultRunnable implements Runnable {
    private int result;

    @Override
    public void run() {
        result = ThreadLocalRandom.current().nextInt(1, 101);
        System.out.println(Thread.currentThread().getName() + " calculo=" + result);
    }

    public int getResult() {
        return result;
    }
}
