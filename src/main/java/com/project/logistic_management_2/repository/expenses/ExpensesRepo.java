package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepo extends JpaRepository<Expenses, String>, ExpensesRepoCustom {
}
