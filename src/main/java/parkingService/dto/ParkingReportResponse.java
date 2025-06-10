package parkingService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingReportResponse {
    private long occupiedSpots;
    private long freeSpots;
    private String averageParkingDuration;

    public ParkingReportResponse(long occupiedSpots, long freeSpots, Duration averageDuration) {
        this.occupiedSpots = occupiedSpots;
        this.freeSpots = freeSpots;
        this.averageParkingDuration = String.format("%02d:%02d:%02d",
                averageDuration.toHours(),
                averageDuration.toMinutesPart(),
                averageDuration.toSecondsPart());
    }

}
