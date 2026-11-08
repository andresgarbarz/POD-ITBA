package ar.edu.itba.pod.mr;

import ar.edu.itba.pod.common.BikeRentalRow;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class RentalsBetweenStationsMapper implements Mapper<String, BikeRentalRow, StationPair, Long> {

    private static final Long ONE = 1L;

    @Override
    public void map(String key, BikeRentalRow value, Context<StationPair, Long> context) {
        if (value.isMember()) {
            context.emit(new StationPair(value.getStartStation(), value.getEndStation()), ONE);
        }
    }
}
