package com.project.logistic_management_2.controller.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.service.truck.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trucks")
@RequiredArgsConstructor
public class TruckController {
    private final TruckService truckService;

    @PostMapping("/create")
    public ResponseEntity<Object> createTruck(@Valid @RequestBody TruckDTO truckDTO) {
        return new ResponseEntity<>(
                BaseResponse.ok(truckService.createTruck(truckDTO)),
                HttpStatus.CREATED
        );
    }

    @GetMapping()
    public ResponseEntity<Object> getAllTrucks() {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.getAllTrucks())
        );
    }

    @GetMapping("/get_by_license/{licensePlate}")
    public ResponseEntity<Object> getTruckByLicensePlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.getTruckByLicensePlate(licensePlate))
        );
    }

    @GetMapping("/get_by_id/{id}")
    public ResponseEntity<Object> getTruckById(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.getTruckById(id))
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateTruck(@PathVariable String id, @Valid @RequestBody TruckDTO truckDTO) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.updateTruck(id, truckDTO))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTruck(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.deleteTruck(id))
        );
    }
}
