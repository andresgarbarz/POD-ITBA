package ar.edu.itba.pod.mr;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class StationPair implements DataSerializable {

    private String startStation;
    private String endStation;

    public StationPair() {
    }

    public StationPair(String startStation, String endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String startStation() {
        return startStation;
    }

    public String endStation() {
        return endStation;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(startStation);
        out.writeUTF(endStation);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        startStation = in.readUTF();
        endStation = in.readUTF();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StationPair stationPair
                && startStation.equals(stationPair.startStation)
                && endStation.equals(stationPair.endStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startStation, endStation);
    }
}
