package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpensesMapper;
import com.project.logistic_management_2.repository.expenses.ExpensesRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl extends BaseService implements ExpensesService {
    private final ExpensesRepo expensesRepo;
    private final ExpensesMapper expensesMapper;

    @Override
    public List<ExpensesDTO> getAll() {
        return expensesRepo.getAll();
    }

    @Override
    public ExpensesDTO getByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        return expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin chi phí!"));
    }

    @Override
    public ExpensesDTO create(ExpensesDTO dto) {
        Expenses expenses = expensesMapper.toExpenses(dto);
        expensesRepo.save(expenses);
        return expensesRepo.getByID(expenses.getId()).get();
    }

    @Override
    public ExpensesDTO update(String id, ExpensesDTO dto) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        ExpensesDTO expensesDTO = expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin chi phí!"));

        Expenses expenses = expensesMapper.toExpenses(expensesDTO);
        expensesMapper.updateExpenses(id, expenses, dto);

        expensesRepo.save(expenses);

        return expensesRepo.getByID(expenses.getId()).get();
    }

    @Override
    public long deleteByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return expensesRepo.delete(id);
    }

    @Override
    public long approveByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return expensesRepo.approve(id);
    }
}
