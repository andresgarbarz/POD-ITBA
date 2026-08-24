package ar.edu.itba.pod.concurrency.iii.e3;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;

/**
 * Benchmar to compare between {@link Arrays#parallelSort(int[])} and
 * {@link Arrays#sort(int[])}
 */
public class SortBenchmark {

    private static final int[] SIZES = {10_000_000, 25_000_000, 50_000_000};
    private static final int RUNS = 4;

    @Test
    public void benchmark_all() {
        for (int size : SIZES) {
            int[] original = randomArray(size);
            long serialMs = 0;
            long parallelMs = 0;

            for (int i = 0; i < RUNS; i++) {
                int[] serialCopy = Arrays.copyOf(original, original.length);
                long start = System.nanoTime();
                Arrays.sort(serialCopy);
                serialMs += (System.nanoTime() - start) / 1_000_000;

                int[] parallelCopy = Arrays.copyOf(original, original.length);
                start = System.nanoTime();
                Arrays.parallelSort(parallelCopy);
                parallelMs += (System.nanoTime() - start) / 1_000_000;
            }

            System.out.printf("%d serial:   %d ms%n", size, serialMs / RUNS);
            System.out.printf("%d parallel: %d ms%n", size, parallelMs / RUNS);
        }
    }

    private static int[] randomArray(int size) {
        int[] data = new int[size];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt();
        }
        return data;
    }
}
