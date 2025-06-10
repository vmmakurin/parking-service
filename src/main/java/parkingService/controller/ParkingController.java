package parkingService.controller;

import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import parkingService.dto.ParkingReportResponse;
import parkingService.dto.VehicleEntryRequest;
import parkingService.dto.VehicleEntryResponse;
import parkingService.dto.VehicleExitRequest;
import parkingService.service.ParkingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.time.LocalDateTime;

@Path("/api/v1/parking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Parking Service", description = "API для управления парковкой")
public class ParkingController {

    @Inject
    ParkingService parkingService;

    @POST
    @Path("/entry")
    @Operation(
            summary = "Регистрация въезда автомобиля",
            description = "Регистрирует въезд автомобиля на парковку"
    )
    @APIResponse(
            responseCode = "200",
            description = "Успешная регистрация",
            content = @Content(schema = @Schema(implementation = VehicleEntryResponse.class))
    )
    public VehicleEntryResponse registerVehicleEntry(VehicleEntryRequest request) {
        return parkingService.registerVehicleEntry(request);
    }

    @POST
    @Path("/exit")
    @Operation(
            summary = "Регистрация выезда автомобиля",
            description = "Регистрирует выезд автомобиля с парковки"
    )
    @APIResponse(
            responseCode = "200",
            description = "Успешная регистрация",
            content = @Content(schema = @Schema(implementation = VehicleEntryResponse.class))
    )
    public VehicleEntryResponse registerVehicleExit(VehicleExitRequest request) {
        return parkingService.registerVehicleExit(request.getVehicleNumber());
    }

    @GET
    @Path("/report")
    @Operation(
            summary = "Генерация отчета по парковке",
            description = "Возвращает статистику по парковке: занятые места, свободные места и среднее время парковки"
    )
    @APIResponse(
            responseCode = "200",
            description = "Успешная генерация отчета",
            content = @Content(schema = @Schema(implementation = ParkingReportResponse.class))
    )
    public ParkingReportResponse generateReport(
            @QueryParam("startDate") String startDateStr,
            @QueryParam("endDate") String endDateStr) {
        LocalDateTime startDate = startDateStr != null ? LocalDateTime.parse(startDateStr) : null;
        LocalDateTime endDate = endDateStr != null ? LocalDateTime.parse(endDateStr) : null;
        return parkingService.generateReport(startDate, endDate);
    }

}
