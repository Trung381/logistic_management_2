package com.project.logistic_management_2.controller.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.service.expenses.expenses.ExpensesService;
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
import java.rmi.ServerException;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpensesController {
    private final ExpensesService expensesService;

    @GetMapping()
    public ResponseEntity<Object> getExpenses(
            @RequestParam Integer page,
            @RequestParam(required = false) String expensesConfigId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.getAll(page, expensesConfigId, truckLicense, fromDate, toDate))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpensesByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpenses(@Valid @RequestBody ExpensesDTO dto) throws ServerException {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateExpenses(@PathVariable String id, @RequestBody ExpensesDTO dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpensesByID(@PathVariable String id) throws ServerException {
        long res = expensesService.deleteByID(id);
        return ResponseEntity.ok(BaseResponse.ok(res, "Đã xóa thành công " + res + " chi phí!"));
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<Object> approveExpensesByID(@PathVariable String id) throws ServerException {
        long res = expensesService.approveByID(id);
        return ResponseEntity.ok(BaseResponse.ok(res, "Đã duyệt thành công " + res + " chi phí!"));
    }

    /**
     * @param driverId id of driver
     * @param period format: yyyy-MM
     */
    @GetMapping("/reports")
    public ResponseEntity<Object> exportReport(@RequestParam String driverId, @RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.report(driverId, period))
        );
    }

    @GetMapping("/reports/all")
    public ResponseEntity<Object> exportReportForAll(@RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.reportForAll(period))
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportExpenses(
            @RequestParam(required = false) String expensesConfigId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) throws Exception {

        List<ExpensesDTO> expenses = expensesService.getAll(null, expensesConfigId, truckLicense, fromDate, toDate);

        if (!CollectionUtils.isEmpty(expenses)) {
            String fileName = "Expenses Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(expenses, fileName, ExportConfig.expensesExport);

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

    @GetMapping("/export/reports")
    public ResponseEntity<Object> exportReportExpenses(
            @RequestParam(required = false) String driverId,
            @RequestParam(required = false) String period
    ) throws Exception {

        List<ExpensesIncurredDTO> expensesReport = expensesService.report(driverId, period);

        if (!CollectionUtils.isEmpty(expensesReport)) {
            String fileName = "ExpensesReport Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(expensesReport, fileName, ExportConfig.expenseReportByDriverExport);

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
    public ResponseEntity<Object> importExpensesData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesService.importExpensesData(importFile)),
                HttpStatus.CREATED
        );
    }
}
