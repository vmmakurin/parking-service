package parkingService.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import parkingService.dto.ParkingReportResponse;
import parkingService.dto.VehicleEntryRequest;
import parkingService.dto.VehicleEntryResponse;
import parkingService.model.ParkingStatus;
import parkingService.model.Vehicle;
import parkingService.repository.VehicleRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService{

    @ConfigProperty(name = "parking.total-spots", defaultValue = "100")
    int maxParkingSpots;

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public VehicleEntryResponse registerVehicleEntry(VehicleEntryRequest request) {

        if (vehicleRepository.findByNumberAndStatus(request.getVehicleNumber(), ParkingStatus.PARKED).isPresent()) {
            throw new RuntimeException("Vehicle is already parked");
        }

        long occupiedSpots = vehicleRepository.countByStatus(ParkingStatus.PARKED);
        if (occupiedSpots >= maxParkingSpots) {
            throw new RuntimeException("No free parking spots available");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setNumber(request.getVehicleNumber());
        vehicle.setType(request.getVehicleType());
        vehicle.setStatus(ParkingStatus.PARKED);
        vehicle.setEntryTime(LocalDateTime.now());
        vehicleRepository.persist(vehicle);

        return new VehicleEntryResponse(vehicle.getNumber(), vehicle.getEntryTime());
    }

    @Override
    @Transactional
    public VehicleEntryResponse registerVehicleExit(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository.findByNumberAndStatus(vehicleNumber, ParkingStatus.PARKED)
                .orElseThrow(() -> new RuntimeException("No active vehicle found"));

        vehicle.setExitTime(LocalDateTime.now());
        vehicle.setStatus(ParkingStatus.EXITED);

        VehicleEntryResponse response = new VehicleEntryResponse();
        response.setVehicleNumber(vehicleNumber);
        response.setEntryTime(vehicle.getEntryTime());

        return response;
    }

    @Override
    public ParkingReportResponse generateReport(LocalDateTime startDate, LocalDateTime endDate) {

        long occupiedSpots = vehicleRepository.countByStatus(ParkingStatus.PARKED);


        long freeSpots = maxParkingSpots - occupiedSpots;


        List<Vehicle> exitedVehicles = vehicleRepository.findByStatus(ParkingStatus.EXITED);

        Duration averageDuration = exitedVehicles.stream()
                .map(vehicle -> Duration.between(vehicle.getEntryTime(), vehicle.getExitTime()))
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(exitedVehicles.isEmpty() ? 1 : exitedVehicles.size());

        return new ParkingReportResponse(occupiedSpots, freeSpots, averageDuration);
    }
}
