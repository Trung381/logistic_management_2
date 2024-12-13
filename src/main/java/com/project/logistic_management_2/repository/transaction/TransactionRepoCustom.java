package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.entity.Transaction;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface TransactionRepoCustom {

    Optional<TransactionDTO> getTransactionsById(String id);
    List<TransactionDTO> getTransactionByFilter(String warehouseId, Boolean origin, Timestamp fromDate, Timestamp toDate);
    long deleteTransaction(String id);

    Float getQuantityByOrigin(String goodsId, Boolean origin ,YearMonth yearMonth);
}
