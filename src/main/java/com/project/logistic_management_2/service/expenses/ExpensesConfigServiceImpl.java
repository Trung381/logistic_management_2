package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.entity.ExpensesConfig;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpensesConfigMapper;
import com.project.logistic_management_2.mapper.expenses.ExpensesMapper;
import com.project.logistic_management_2.repository.expenses.ExpensesConfigRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesConfigServiceImpl extends BaseService implements ExpensesConfigService {
    private final ExpensesConfigRepo expensesConfigRepo;
    private final ExpensesConfigMapper expensesConfigMapper;

    /**
     * Get all expenses configurations
     *
     * @return all expenses configs
     */
    @Override
    public List<ExpensesConfigDTO> getAll() {
        return expensesConfigRepo.getAll();
    }

    /**
     * Get expenses configuration by id
     *
     * @param id: String
     * @return expenses with id
     */
    @Override
    public ExpensesConfigDTO getByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        ExpensesConfig config = expensesConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin cấu hình chi phí!"));
        return expensesConfigMapper.toExpensesConfigDTO(null, config);
    }

    @Override
    public ExpensesConfigDTO create(ExpensesConfigDTO dto) {
        ExpensesConfig config = expensesConfigMapper.toExpensesConfig(dto);
        expensesConfigRepo.save(config);
        return expensesConfigMapper.toExpensesConfigDTO(dto, config);
    }

    @Override
    public ExpensesConfigDTO update(String id, ExpensesConfigDTO dto) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        ExpensesConfig config = expensesConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin cấu hình chi phí!"));
        expensesConfigMapper.updateExpensesConfig(config, dto);

        //Lưu lại cập nhật vào db và trả về dto từ mapper
        return expensesConfigMapper.toExpensesConfigDTO(dto, expensesConfigRepo.save(config));
    }

    @Override
    public long deleteByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return expensesConfigRepo.delete(id);
    }
}
