package parkingService.service;

import parkingService.dto.ParkingReportResponse;
import parkingService.dto.VehicleEntryRequest;
import parkingService.dto.VehicleEntryResponse;

import java.time.LocalDateTime;

public interface ParkingService {
    VehicleEntryResponse registerVehicleEntry(VehicleEntryRequest request);
    VehicleEntryResponse registerVehicleExit(String vehicleNumber);
    ParkingReportResponse generateReport(LocalDateTime startDate, LocalDateTime endDate);
}
