package com.project.logistic_management_2.repository.salarydeduction;

import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.repository.rolePermission.RolePermissionRepoCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryDeductionRepo extends JpaRepository<SalaryDeduction, Integer>, SalaryDeductionRepoCustom {
}
