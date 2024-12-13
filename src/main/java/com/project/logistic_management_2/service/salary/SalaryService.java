package com.project.logistic_management_2.service.salary;

import com.project.logistic_management_2.dto.salary.SalaryUserDTO;
import com.project.logistic_management_2.dto.salary.UpdateSalaryDTO;
import com.project.logistic_management_2.entity.Salary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SalaryService {
    List<SalaryUserDTO> getAllSalaryWithUserPeriod(String period);
    SalaryUserDTO getSalaryWithUser(Integer id);
    SalaryUserDTO updateSalary(Integer id, UpdateSalaryDTO updateSalaryDTO);
    void createSalaryForAllUsers();
}
