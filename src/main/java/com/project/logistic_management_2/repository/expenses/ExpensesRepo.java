package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpensesRepo extends JpaRepository<Expenses, String>, ExpensesRepoCustom {
    @Override
    Optional<Expenses> findById(@NonNull String id);
}
