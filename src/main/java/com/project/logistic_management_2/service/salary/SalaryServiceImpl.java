package com.project.logistic_management_2.service.salary;

import com.project.logistic_management_2.dto.salary.SalaryUserDTO;
import com.project.logistic_management_2.dto.salary.UpdateSalaryDTO;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.repository.salary.SalaryRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SalaryServiceImpl extends BaseService implements SalaryService {

    private final SalaryRepo salaryRepo;
    private final PermissionType type = PermissionType.SALARIES;

    @Override
    public List<SalaryUserDTO> getAllSalaryWithUserPeriod(String period) {
        checkPermission(type, PermissionKey.VIEW);
        return salaryRepo.getAllSalaryWithUserPeriod(period);
    }

    @Override
    public SalaryUserDTO getSalaryWithUser(Integer id) {
        checkPermission(type, PermissionKey.VIEW);
        return salaryRepo.getSalaryWithUser(id);
    }

    @Override
    public SalaryUserDTO updateSalary(Integer id, UpdateSalaryDTO updateSalaryDTO) {

        checkPermission(type, PermissionKey.WRITE);

        Boolean updated = salaryRepo.updateSalary(id, updateSalaryDTO);

        SalaryUserDTO updatedSalary = null;
        if(updated){
            updatedSalary = salaryRepo.getSalaryWithUser(id);
        }

        return updatedSalary;
    }

    public void createSalaryForAllUsers(){
        salaryRepo.createSalaryForAllUsers();
    }
}
