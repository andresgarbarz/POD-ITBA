package ar.edu.itba.pod.common;

import java.time.LocalDateTime;

public class BikeRentalRow {

    private String startStation, endStation;
    private LocalDateTime startDate, endDate;
    private boolean isMember;

    public BikeRentalRow(String startStation,
                         LocalDateTime startDate,
                         String endStation,
                         LocalDateTime endDate,
                         boolean isMember) {
        this.startStation = startStation;
        this.startDate = startDate;
        this.endStation = endStation;
        this.endDate = endDate;
        this.isMember = isMember;
    }

    public BikeRentalRow() {
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isMember() {
        return isMember;
    }

}
