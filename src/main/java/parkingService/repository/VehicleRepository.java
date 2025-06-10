package parkingService.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import parkingService.model.ParkingStatus;
import parkingService.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {

    public Optional<Vehicle> findByNumberAndStatus(String number, ParkingStatus status) {
        return find("number = ?1 and status = ?2", number, status).firstResultOptional();
    }

    public List<Vehicle> findByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return find("entryTime >= ?1 and (exitTime <= ?2 or exitTime is null)", startDate, endDate).list();
    }

    public long countByStatus(ParkingStatus status) {
        return count("status", status);
    }

    public List<Vehicle> findByStatus(ParkingStatus status) {
        return list("status", status);
    }

}
