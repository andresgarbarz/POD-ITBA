package ar.edu.itba.pod.concurrency.exercises.e2;

/**
 * Runnable de la clase: duerme en un loop. Si lo interrumpen, sale.
 */
public class SleeperRunnable implements Runnable {
    private final int naps;
    private final long napMillis;

    public SleeperRunnable() {
        this(10, 1000);
    }

    public SleeperRunnable(int naps, long napMillis) {
        this.naps = naps;
        this.napMillis = napMillis;
    }

    @Override
    public void run() {
        for (int i = 0; i < naps; i++) {
            try {
                System.out.println(Thread.currentThread().getName() + " - siesta numero: " + i);
                Thread.sleep(napMillis);
            } catch (InterruptedException e) {
                // sleep() tira InterruptedException y limpia el flag; hay que salir.
                System.out.println(Thread.currentThread().getName() + " - Interrupted");
                return;
            }
        }
        System.out.println(Thread.currentThread().getName() + " - termino todas las siestas");
    }
}
