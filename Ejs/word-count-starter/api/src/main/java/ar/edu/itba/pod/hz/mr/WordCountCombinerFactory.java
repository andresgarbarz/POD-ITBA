package ar.edu.itba.pod.hz.mr;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class WordCountCombinerFactory implements CombinerFactory<String, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(String key) {
        return new WordCountCombiner();
    }

    private static class WordCountCombiner extends Combiner<Long, Long> {
        private long sum;

        @Override
        public void reset() {
            sum = 0L;
        }

        @Override
        public void combine(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }
    }
}
