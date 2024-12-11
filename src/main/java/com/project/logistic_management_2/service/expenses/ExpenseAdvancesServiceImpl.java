package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpenseAdvancesDTO;
import com.project.logistic_management_2.entity.ExpenseAdvances;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpenseAdvancesMapper;
import com.project.logistic_management_2.repository.expenses.ExpenseAdvancesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseAdvancesServiceImpl implements ExpenseAdvancesService {
    private final ExpenseAdvancesRepo expenseAdvancesRepo;
    private final ExpenseAdvancesMapper expenseAdvancesMapper;

    @Override
    public List<ExpenseAdvancesDTO> getAll() {
        return expenseAdvancesRepo.getAll();
    }

    @Override
    public ExpenseAdvancesDTO getByDriverId(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return expenseAdvancesRepo.getByDriverId(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin!"));
    }



    @Override
    public long delete(Integer id) {
        return expenseAdvancesRepo.deleted(id);
    }

    @Override
    public ExpenseAdvancesDTO update(Integer id, ExpenseAdvancesDTO dto) {
        ExpenseAdvancesDTO expenseAdvancesDTO = expenseAdvancesRepo.getExpenseAdvanceById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin!"));
        ExpenseAdvances expenseAdvances = expenseAdvancesMapper.toExpenseAdvances(dto);
        expenseAdvancesMapper.updateExpenseAdvance(id, expenseAdvances, dto);
        expenseAdvancesRepo.save(expenseAdvances);
        return expenseAdvancesRepo.getExpenseAdvanceById(id).get();
    }

    @Override
    public ExpenseAdvancesDTO create(ExpenseAdvancesDTO dto) {
        ExpenseAdvances expenseAdvances = expenseAdvancesMapper.toExpenseAdvances(dto);
        return expenseAdvancesRepo.getExpenseAdvanceById(
                expenseAdvancesRepo.save(expenseAdvances).getId()
        ).get();
    }
}
