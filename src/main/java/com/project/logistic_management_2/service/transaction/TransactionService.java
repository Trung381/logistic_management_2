package com.project.logistic_management_2.service.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;

@Service
public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO updateTransaction(String id ,TransactionDTO transactionDTO);
    long deleteTransaction(String id);
    TransactionDTO getTransactionById(String id);
    List<TransactionDTO> getTransactionByFilter(String warehouseId, Boolean origin, Timestamp fromDate, Timestamp toDate);
}
