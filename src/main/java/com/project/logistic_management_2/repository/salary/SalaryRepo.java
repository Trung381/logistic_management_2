package com.project.logistic_management_2.repository.salary;

import com.project.logistic_management_2.entity.salary.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepo extends JpaRepository<Salary, Integer>, SalaryRepoCustom {
}
