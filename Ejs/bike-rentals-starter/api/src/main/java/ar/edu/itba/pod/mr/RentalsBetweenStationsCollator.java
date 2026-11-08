package ar.edu.itba.pod.mr;

import ar.edu.itba.pod.common.RentalsBetweenStationsResult;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class RentalsBetweenStationsCollator
        implements Collator<Map.Entry<StationPair, Long>, SortedSet<RentalsBetweenStationsResult>> {

    @Override
    public SortedSet<RentalsBetweenStationsResult> collate(Iterable<Map.Entry<StationPair, Long>> values) {
        SortedSet<RentalsBetweenStationsResult> result = new TreeSet<>(
                Comparator.comparing(RentalsBetweenStationsResult::rentals).reversed()
                        .thenComparing(RentalsBetweenStationsResult::startStation)
                        .thenComparing(RentalsBetweenStationsResult::endStation));
        for (Map.Entry<StationPair, Long> entry : values) {
            result.add(new RentalsBetweenStationsResult(
                    entry.getKey().startStation(),
                    entry.getKey().endStation(),
                    entry.getValue()));
        }
        return result;
    }
}
