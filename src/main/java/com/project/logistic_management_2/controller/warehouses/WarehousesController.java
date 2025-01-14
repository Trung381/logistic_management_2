package com.project.logistic_management_2.controller.warehouses;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.service.warehouses.WarehousesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @GetMapping("/export")
    public ResponseEntity<Object> exportWarehouses() throws Exception {

        ExportExcelResponse exportExcelResponse = warehousesService.exportWarehouses();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}
