package com.project.logistic_management_2.controller.transaction;

import com.mysema.commons.lang.Pair;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.service.transaction.TransactionService;
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
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Object> createTransaction(@Valid @RequestBody TransactionDTO dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(transactionService.createTransaction(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateTransaction(@PathVariable String id, @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(BaseResponse.ok(transactionService.updateTransaction(id, dto)));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable String id) {
        return ResponseEntity.ok(BaseResponse.ok(transactionService.deleteTransaction(id)));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(BaseResponse.ok(transactionService.getTransactionById(id)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getTransactionByFilter(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) Boolean origin,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        List<TransactionDTO> transactions = transactionService.getTransactionByFilter(page, warehouseId, origin, dateRange.getFirst(), dateRange.getSecond());

        return ResponseEntity.ok(BaseResponse.ok(transactions));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportTransaction(
            @RequestParam int page,
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) Boolean origin,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) throws Exception {
        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        List<TransactionDTO> transactions = transactionService.getTransactionByFilter(page, warehouseId, origin, dateRange.getFirst(), dateRange.getSecond());


        if (!CollectionUtils.isEmpty(transactions)) {
            String fileName = "Transactions Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(transactions, fileName, ExportConfig.transactionExport);

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
    public ResponseEntity<Object> importTransactionData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(transactionService.importTransactionData(importFile)),
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
