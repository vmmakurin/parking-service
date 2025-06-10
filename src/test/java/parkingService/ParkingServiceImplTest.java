package parkingService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parkingService.dto.*;
import parkingService.model.VehicleType;
import parkingService.repository.VehicleRepository;
import parkingService.service.ParkingService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
class ParkingServiceImplTest {
    @Inject
    ParkingService parkingService;

    @Inject
    VehicleRepository vehicleRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    void registerVehicleEntry_Success() {
        VehicleEntryRequest request = new VehicleEntryRequest("A123BC", VehicleType.PASSENGER);
        VehicleEntryResponse response = parkingService.registerVehicleEntry(request);

        assertNotNull(response);
        assertEquals("A123BC", response.getVehicleNumber());
        assertNotNull(response.getEntryTime());
    }

    @Test
    void registerVehicleEntry_ParkingFull() {

        for (int i = 0; i < 3; i++) {
            VehicleEntryRequest request = new VehicleEntryRequest("A" + i, VehicleType.PASSENGER);
            parkingService.registerVehicleEntry(request);
        }

        VehicleEntryRequest request = new VehicleEntryRequest("A999", VehicleType.PASSENGER);
        assertThrows(RuntimeException.class, () -> parkingService.registerVehicleEntry(request));
    }

    @Test
    void registerVehicleEntry_DuplicateVehicle() {

        VehicleEntryRequest request = new VehicleEntryRequest("A123BC", VehicleType.PASSENGER);
        parkingService.registerVehicleEntry(request);

        assertThrows(RuntimeException.class, () -> parkingService.registerVehicleEntry(request));
    }

    @Test
    void registerVehicleExit_Success() {
        VehicleEntryRequest entryRequest = new VehicleEntryRequest("A123BC", VehicleType.PASSENGER);
        parkingService.registerVehicleEntry(entryRequest);

        VehicleEntryResponse response = parkingService.registerVehicleExit("A123BC");

        assertNotNull(response);
        assertEquals("A123BC", response.getVehicleNumber());
        assertNotNull(response.getEntryTime());
    }

    @Test
    void registerVehicleExit_VehicleNotFound() {
        assertThrows(RuntimeException.class, () -> parkingService.registerVehicleExit("NONEXISTENT"));
    }

    @Test
    void registerVehicleExit_AlreadyExited() {
        VehicleEntryRequest entryRequest = new VehicleEntryRequest("A123BC", VehicleType.PASSENGER);
        parkingService.registerVehicleEntry(entryRequest);

        parkingService.registerVehicleExit("A123BC");

        assertThrows(RuntimeException.class, () -> parkingService.registerVehicleExit("A123BC"));
    }

    @Test
    void generateReport() {

        parkingService.registerVehicleEntry(new VehicleEntryRequest("A1", VehicleType.PASSENGER));
        parkingService.registerVehicleEntry(new VehicleEntryRequest("A2", VehicleType.TRUCK));
        parkingService.registerVehicleEntry(new VehicleEntryRequest("A3", VehicleType.MOTORCYCLE));

        ParkingReportResponse report = parkingService.generateReport(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1)
        );

        assertEquals(3, report.getOccupiedSpots());
        assertEquals(0, report.getFreeSpots());
        assertNotNull(report.getAverageParkingDuration());
    }

}
