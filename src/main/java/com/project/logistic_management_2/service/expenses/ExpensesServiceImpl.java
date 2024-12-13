package com.project.logistic_management_2.service.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpensesMapper;
import com.project.logistic_management_2.repository.expenses.ExpensesRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl extends BaseService implements ExpensesService {
    private final ExpensesRepo expensesRepo;
    private final ExpensesMapper expensesMapper;
    private final NotificationService notificationService;
    private final PermissionType type = PermissionType.EXPENSES;

    @Override
    public List<ExpensesDTO> getAll() {
        checkPermission(type, PermissionKey.VIEW);
        return expensesRepo.getAll(null, null);
    }

    @Override
    public ExpensesDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        return expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin chi phí!"));
    }

    @Override
    public ExpensesDTO create(ExpensesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        Expenses expenses = expensesMapper.toExpenses(dto);
        expensesRepo.save(expenses);

        String notifyMsg = "Có một chi phí được tạo mới cần được phê duyệt lúc " + new Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        return expensesRepo.getByID(expenses.getId()).get();
    }

    @Override
    public ExpensesDTO update(String id, ExpensesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        ExpensesDTO expensesDTO = expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin chi phí!"));

        //Ánh xạ từ DTO sang đối tượng kiểu Expenses để cập nhật
        Expenses expenses = expensesMapper.toExpenses(expensesDTO);
        expensesMapper.updateExpenses(id, expenses, dto);

        //Lưu kết quả
        expensesRepo.save(expenses);

        return expensesRepo.getByID(expenses.getId()).get();
    }

    @Override
    public long deleteByID(String id) {
        checkPermission(type, PermissionKey.DELETE);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return expensesRepo.delete(id);
    }

    @Override
    public long approveByID(String id) {
        checkPermission(type, PermissionKey.APPROVE);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return expensesRepo.approve(id);
    }

    // thees moiws cos chuwcs nanwg bao cao
    // thang dc xem bao cao la chuc cao nhat roi. quyenn all

    public List<ExpensesDTO> report(String driverId, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        //Check định dạng chu kỳ: yyyy-MM
        String regex = "^(\\d{4}-(0[1-9]|1[0-2]))$";
        if (!Pattern.matches(regex, period)) {
            throw new InvalidParameterException("Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM");
        }

        YearMonth periodYM = YearMonth.parse(period);

        return expensesRepo.getAll(driverId, periodYM);
    }

    @Override
    public List<ExpensesReportDTO> reportForAll(String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        return expensesRepo.reportForAll(period);
    }
}
