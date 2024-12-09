package com.project.logistic_management_2.repository.salaryreceived;

import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.entity.SalaryReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryReceivedRepo extends JpaRepository<SalaryReceived, Integer>, SalaryReceivedRepoCustom {
}
