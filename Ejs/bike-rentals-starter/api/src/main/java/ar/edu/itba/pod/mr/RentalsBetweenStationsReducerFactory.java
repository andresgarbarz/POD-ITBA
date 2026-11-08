package ar.edu.itba.pod.mr;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class RentalsBetweenStationsReducerFactory implements ReducerFactory<StationPair, Long, Long> {

    @Override
    public Reducer<Long, Long> newReducer(StationPair key) {
        return new RentalsBetweenStationsReducer();
    }

    private static class RentalsBetweenStationsReducer extends Reducer<Long, Long> {
        private long sum;

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
