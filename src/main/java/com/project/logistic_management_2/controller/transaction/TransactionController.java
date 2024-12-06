package com.project.logistic_management_2.controller.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.dto.response.BaseResponse;
import com.project.logistic_management_2.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> updateTransaction(
            @PathVariable String id,
            @Valid @RequestBody TransactionDTO dto) {

        return ResponseEntity.ok(
                BaseResponse.ok(transactionService.updateTransaction(id, dto))
        );
    }

    @PostMapping("/delete/{id}")
    public void deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getTransactionByFilter(
            @RequestParam(required = false) String wareHouseId,
            @RequestParam(required = false) Boolean origin,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Timestamp fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Timestamp toDate) {

        List<TransactionDTO> transactions = transactionService.getTransactionByFilter(wareHouseId, origin, fromDate, toDate);

        return ResponseEntity.ok(BaseResponse.ok(transactions));
    }
}
