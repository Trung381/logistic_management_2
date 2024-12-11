package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.entity.ExpenseAdvances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseAdvancesRepo extends JpaRepository<ExpenseAdvances, Integer>, ExpenseAdvancesRepoCustom {
}
