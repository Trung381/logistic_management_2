package com.project.logistic_management_2.repository.transaction;

import com.project.logistic_management_2.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String>, TransactionRepoCustom {
}
