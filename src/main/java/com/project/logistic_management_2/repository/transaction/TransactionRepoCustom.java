package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.entity.Transaction;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface TransactionRepoCustom {

    Optional<Transaction> getTransactionsById(String id);
    List<Transaction> getTransactionByFilter(String wareHouseId, Boolean origin, Timestamp fromDate, Timestamp toDate);
}
