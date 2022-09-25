package app.warehouse.system.controller;

import app.warehouse.system.dto.TruckDto;
import app.warehouse.system.service.TruckService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/truck")
@AllArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @GetMapping(value = "/")
    public ResponseEntity<Page<TruckDto>> getAllActiveTrucks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "licensePlate") String sortBy) {
        return ResponseEntity.ok(truckService.getAllActiveTrucks(pageNo, pageSize, sortBy));
    }

    @GetMapping(value = "/id")
    public ResponseEntity<TruckDto> getTruckById(@RequestParam(name = "truckId") Long truckId) {
        return ResponseEntity.ok(truckService.getTruckById(truckId));
    }
}
