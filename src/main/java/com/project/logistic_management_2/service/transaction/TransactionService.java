package com.project.logistic_management_2.service.transaction;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.entity.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    Optional<TransactionDTO> updateTransaction(String id, TransactionDTO dto);
    String deleteTransaction(String id);
    TransactionDTO getTransactionById(String id);
    List<TransactionDTO> getTransactionByFilter(int page, String warehouseId, Boolean origin, String fromDateStr, String toDateStr);
    List<Transaction> importTransactionData(MultipartFile importFile);
    ExportExcelResponse exportTransaction(int page, String warehouseId, Boolean origin, String fromDate, String toDate) throws Exception;
}
