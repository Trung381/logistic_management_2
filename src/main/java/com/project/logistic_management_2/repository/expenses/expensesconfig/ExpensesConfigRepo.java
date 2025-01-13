package com.project.logistic_management_2.repository.expenses.expensesconfig;

import com.project.logistic_management_2.entity.expenses.ExpensesConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesConfigRepo extends JpaRepository<ExpensesConfig, String>, ExpensesConfigRepoCustom {
}
