package ar.edu.itba.pod.concurrency.exercises.e2;

/**
 * Lanza dos SleeperRunnable.
 * Version de la clase: interrumpe al segundo thread y espera al primero.
 * Version pedida: en vez de interrupt, imprime los nombres mientras viven.
 *
 * Correr el main (este si tiene main) y poner breakpoints en sleep / interrupt / join.
 */
public class SleepyThreads {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== original: interrupt + join ===");
        runWithInterrupt();

        System.out.println("=== pedido: ciclo imprimiendo nombres ===");
        runPrintingNames();
    }

    /** Slide original: arranca 2 threads, interrumpe sl-1, espera a sl-0. */
    static void runWithInterrupt() throws InterruptedException {
        Thread[] ts = startSleepers(5, 200);
        ts[1].interrupt();
        ts[0].join();
        ts[1].join();
    }

    /** En vez de interrupt: mientras haya alguno vivo, imprimir nombres y estado. */
    static void runPrintingNames() throws InterruptedException {
        Thread[] ts = startSleepers(5, 200);
        while (ts[0].isAlive() || ts[1].isAlive()) {
            for (Thread t : ts) {
                System.out.printf("%s isAlive=%s state=%s%n", t.getName(), t.isAlive(), t.getState());
            }
            Thread.sleep(150);
        }
        for (Thread t : ts) {
            t.join();
        }
    }

    private static Thread[] startSleepers(int naps, long napMillis) {
        Thread[] ts = new Thread[2];
        for (int i = 0; i < ts.length; i++) {
            Thread thread = new Thread(new SleeperRunnable(naps, napMillis), "sl-" + i);
            thread.start();
            ts[i] = thread;
        }
        return ts;
    }
}
