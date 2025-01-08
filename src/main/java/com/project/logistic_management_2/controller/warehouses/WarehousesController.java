package com.project.logistic_management_2.controller.warehouses;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.request.WarehousesDTO;
import com.project.logistic_management_2.service.warehouses.WarehousesService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        List<WarehousesDTO> warehouses = warehousesService.getAllWarehouses();


        if (!CollectionUtils.isEmpty(warehouses)) {
            String fileName = "Warehouses Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(warehouses, fileName, ExportConfig.warehouseExport);

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
}
