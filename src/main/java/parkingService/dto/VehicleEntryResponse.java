package parkingService.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntryResponse {
    private String vehicleNumber;
    private LocalDateTime entryTime;
}
