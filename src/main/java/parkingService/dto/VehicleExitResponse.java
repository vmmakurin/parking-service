package parkingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleExitResponse {
    private String vehicleNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}
