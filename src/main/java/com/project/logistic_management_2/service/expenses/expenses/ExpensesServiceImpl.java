package com.project.logistic_management_2.service.expenses.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.expenses.ExpensesMapper;
import com.project.logistic_management_2.repository.expenses.expenses.ExpensesRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.notification.NotificationService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl extends BaseService implements ExpensesService {
    private final ExpensesRepo expensesRepo;
    private final ExpensesMapper expensesMapper;
    private final NotificationService notificationService;
    private final PermissionType type = PermissionType.EXPENSES;

    @Override
    public List<ExpensesDTO> getAll(String expensesConfigId, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        checkPermission(type, PermissionKey.VIEW);
        return expensesRepo.getAll(expensesConfigId, truckLicense, fromDate, toDate);
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

        return expensesRepo.getByID(expenses.getId()).orElse(null);
    }

    @Override
    public ExpensesDTO update(String id, ExpensesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        // Return not found message if expenses does not exist
        Expenses expenses = expensesRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi phí cần cập nhật không tồn tại!"));
        // Update expenses object
        expensesMapper.updateExpenses(expenses, dto);
        // Save to DB
        expensesRepo.save(expenses);

        Optional<ExpensesDTO> res = expensesRepo.getByID(expenses.getId());
        return res.orElse(null);
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

    @Override
    public List<ExpensesIncurredDTO> report(String driverId, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        //Check định dạng chu kỳ: yyyy-MM
        String regex = "^(\\d{4}-(0[1-9]|1[0-2]))$";
        if (!Pattern.matches(regex, period)) {
            throw new InvalidParameterException("Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM");
        }

        YearMonth periodYM = YearMonth.parse(period);

        return expensesRepo.getByFilter(driverId, periodYM);
    }

    @Override
    public List<ExpensesReportDTO> reportForAll(String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        return expensesRepo.reportForAll(period);
    }

    @Override
    public List<Expenses> importExpensesData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ExpensesDTO> expensesDTOList = ExcelUtils.getImportData(workbook, ImportConfig.expensesImport);

        List<Expenses> expenses = expensesMapper.toExpensesList(expensesDTOList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return expensesRepo.saveAll(expenses);
    }
}
