package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.dto.transaction.TransactionDTO;
import com.project.logistic_management_2.entity.transaction.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TransactionRepoCustom {

    Optional<TransactionDTO> getTransactionsById(String id);
    List<TransactionDTO> getTransactionByFilter(int page, String warehouseId, Boolean origin, Date fromDate, Date toDate);
    long deleteTransaction(String id);
    long updateTransaction(Transaction transaction, String id, TransactionDTO dto);
    Float getQuantityByOrigin(String goodsId, Boolean origin , Date fromDate, Date toDate);
}
