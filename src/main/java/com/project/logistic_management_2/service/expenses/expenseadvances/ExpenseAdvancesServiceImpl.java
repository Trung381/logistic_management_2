package com.project.logistic_management_2.service.expenses.expenseadvances;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;
import com.project.logistic_management_2.entity.expenses.ExpenseAdvances;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpenseAdvancesMapper;
import com.project.logistic_management_2.repository.expenses.expenseadvances.ExpenseAdvancesRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseAdvancesServiceImpl extends BaseService implements ExpenseAdvancesService {
    private final ExpenseAdvancesRepo expenseAdvancesRepo;
    private final ExpenseAdvancesMapper expenseAdvancesMapper;
    private final PermissionType type = PermissionType.EXPENSES;

    @Override
    public List<ExpenseAdvancesDTO> getAll(int page) {
        checkPermission(type, PermissionKey.VIEW);
        return expenseAdvancesRepo.getAll(page);
    }

    @Override
    public ExpenseAdvancesDTO getByDriverId(String id) {
        checkPermission(type, PermissionKey.VIEW);
        return expenseAdvancesRepo.getByDriverId(id)
                .orElseThrow(() -> new NotFoundException("Thông tin ứng chi phí không tồn tại!"));
    }

    @Override
    public long delete(Integer id) {
        checkPermission(type, PermissionKey.DELETE);

        return expenseAdvancesRepo.deleted(id);
    }

    @Override
    public ExpenseAdvancesDTO update(Integer id, ExpenseAdvancesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        ExpenseAdvancesDTO expenseAdvancesDTO = expenseAdvancesRepo.getExpenseAdvanceById(id)
                .orElseThrow(() -> new NotFoundException("Thông tin ứng chi phí không tồn tại!"));
        ExpenseAdvances expenseAdvances = expenseAdvancesMapper.toExpenseAdvances(expenseAdvancesDTO);
        expenseAdvancesMapper.updateExpenseAdvance(id, expenseAdvances, dto);
        expenseAdvancesRepo.save(expenseAdvances);
        return expenseAdvancesRepo.getExpenseAdvanceById(id).get();
    }

    @Override
    public ExpenseAdvancesDTO create(ExpenseAdvancesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        ExpenseAdvances expenseAdvances = expenseAdvancesMapper.toExpenseAdvances(dto);
        return expenseAdvancesRepo.getExpenseAdvanceById(
                expenseAdvancesRepo.save(expenseAdvances).getId()
        ).get();
    }
}
