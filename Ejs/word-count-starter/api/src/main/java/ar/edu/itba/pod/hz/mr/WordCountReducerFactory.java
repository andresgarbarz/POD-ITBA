package ar.edu.itba.pod.hz.mr;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class WordCountReducerFactory implements ReducerFactory<String, Long, Long> {

    @Override
    public Reducer<Long, Long> newReducer(String key) {
        return new WordCountReducer();
    }

    private static class WordCountReducer extends Reducer<Long, Long> {
        private volatile long sum;

        @Override
        public void reduce(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
