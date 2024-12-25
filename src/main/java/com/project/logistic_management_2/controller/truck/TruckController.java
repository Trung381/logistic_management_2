package com.project.logistic_management_2.controller.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.service.truck.TruckService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    @GetMapping("/export")
    public ResponseEntity<Object> exportTruck() throws Exception {
        List<TruckDTO> trucks = truckService.getAllTrucks();

        if (!CollectionUtils.isEmpty(trucks)) {
            String fileName = "Trucks Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(trucks, fileName, ExportConfig.truckExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");

        }
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importTruckData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(truckService.importTruckData(importFile)),
                HttpStatus.CREATED
        );
    }
}
