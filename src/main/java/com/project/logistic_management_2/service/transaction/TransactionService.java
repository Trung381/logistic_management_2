package com.project.logistic_management_2.service.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO updateTransaction(String id ,TransactionDTO transactionDTO);
    TransactionDTO deleteTransaction(String id);
    List<TransactionDTO> getTransactionByFilter(String wareHouseId, Boolean origin, Timestamp fromDate, Timestamp toDate);
}
