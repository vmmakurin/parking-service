package parkingService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import parkingService.model.VehicleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntryRequest {
    private String vehicleNumber;
    private VehicleType vehicleType;
}
