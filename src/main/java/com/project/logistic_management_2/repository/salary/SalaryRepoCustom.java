package com.project.logistic_management_2.repository.salary;

import com.project.logistic_management_2.dto.salary.SalaryUserDTO;
import com.project.logistic_management_2.dto.salary.UpdateSalaryDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepoCustom {
    List<SalaryUserDTO> getAllSalaryWithUserPeriod(String period);
    SalaryUserDTO getSalaryWithUser(Integer id);
    Boolean updateSalary(Integer id, UpdateSalaryDTO updateSalaryDTO);
    void deleteSalary(Integer salaryId);
    void createSalaryForAllUsers();
}
