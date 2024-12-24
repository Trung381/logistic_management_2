package com.project.logistic_management_2.controller.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.service.expenses.ExpensesConfigService;
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
@RequestMapping("/expenses/configs")
@RequiredArgsConstructor
public class ExpensesConfigController {
    private final ExpensesConfigService expensesConfigService;

    @GetMapping()
    public ResponseEntity<Object> getExpensesConfigs() {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpensesConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpensesConfig(@Valid @RequestBody ExpensesConfigDTO dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesConfigService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateExpensesConfig(@PathVariable String id, @Valid @RequestBody ExpensesConfigDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpensesConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.deleteByID(id))
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportExpensesConfig() throws Exception {

        List<ExpensesConfigDTO> expensesConfig = expensesConfigService.getAll();


        if (!CollectionUtils.isEmpty(expensesConfig)) {
            String fileName = "ExpensesConfig Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(expensesConfig, fileName, ExportConfig.expensesConfigExport);

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
    public ResponseEntity<Object> importExpensesConfigData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesConfigService.importExpensesConfigData(importFile)),
                HttpStatus.CREATED
        );
    }
}
