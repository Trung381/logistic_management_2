package com.project.logistic_management_2.controller.expenses;

import com.mysema.commons.lang.Pair;
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
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpensesController {
    private final ExpensesService expensesService;

    @GetMapping()
    public ResponseEntity<Object> getExpenses(
            @RequestParam int page,
            @RequestParam(required = false) String expensesConfigId,
            @RequestParam(required = false) String truckLicense,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.getAll(page, expensesConfigId, truckLicense, dateRange.getFirst(), dateRange.getSecond()))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpensesByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpenses(@Valid @RequestBody ExpensesDTO dto) {
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
        return res != -1
                ? ResponseEntity.ok(BaseResponse.ok(res, "Đã duyệt thành công " + res + " chi phí!"))
                : ResponseEntity.ok(BaseResponse.ok(null, "Chi phí đã được duyệt trước đó!"));
    }

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
        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        List<ExpensesDTO> expenses = expensesService.getAll(expensesConfigId, truckLicense, dateRange.getFirst(), dateRange.getSecond());


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
            @RequestParam(required = false) String period) throws Exception {

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

    private Pair<Timestamp, Timestamp> parseAndValidateDates(String fromDate, String toDate) throws IllegalArgumentException {
        Timestamp fromTimestamp = null;
        Timestamp toTimestamp = null;

        if (fromDate != null) {
            try {
                fromTimestamp = Timestamp.valueOf(fromDate.replace("T", " ") + ".000");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid fromDate format.");
            }
        }

        if (toDate != null) {
            try {
                toTimestamp = Timestamp.valueOf(toDate.replace("T", " ") + ".000");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid toDate format.");
            }
        }

        return Pair.of(fromTimestamp, toTimestamp);
    }
}
