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

    @GetMapping("/get_by_license/{license}")
    public ResponseEntity<Object> getTruckByLicensePlate(@PathVariable String license) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.getTruckByLicensePlate(license))
        );
    }

    @GetMapping("/get_by_type/{type}")
    public ResponseEntity<Object> getTrucksByType(@PathVariable Integer type) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.getTrucksByType(type))
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateTruck(@PathVariable Integer id, @Valid @RequestBody TruckDTO truckDTO) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.updateTruck(id, truckDTO))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTruck(@PathVariable Integer id) {
        return ResponseEntity.ok(
                BaseResponse.ok(truckService.deleteTruck(id))
        );
    }
}
