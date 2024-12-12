package com.project.logistic_management_2.controller.warehouses;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.service.warehouses.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehousesController {
    private final WarehousesService warehousesService;

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllWarehouses() {
        return ResponseEntity.ok(
                BaseResponse.ok(warehousesService.getAllWarehouses())
        );
    }
}
