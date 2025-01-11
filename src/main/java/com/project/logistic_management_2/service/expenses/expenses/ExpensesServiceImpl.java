package com.project.logistic_management_2.service.expenses.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.attached.AttachedType;
import com.project.logistic_management_2.enums.expenses.ExpensesStatus;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.ConflictException;
import com.project.logistic_management_2.exception.define.InvalidParameterException;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.exception.define.NotModifiedException;
import com.project.logistic_management_2.mapper.expenses.ExpensesMapper;
import com.project.logistic_management_2.repository.expenses.expenses.ExpensesRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.attached.AttachedImageService;
import com.project.logistic_management_2.service.notification.NotificationService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import com.project.logistic_management_2.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl extends BaseService implements ExpensesService {
    private final ExpensesRepo expensesRepo;
    private final ExpensesMapper expensesMapper;
    private final NotificationService notificationService;
    private final AttachedImageService attachedService;
    private final PermissionType type = PermissionType.EXPENSES;

    @Override
    public List<ExpensesDTO> getAll(int page, String expensesConfigId, String truckLicense, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKey.VIEW);
        if (page <= 0) {
            throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
        }
        Date fromDate = Utils.convertToDateOfTimestamp(fromDateStr);
        Date toDate = Utils.convertToDateOfTimestamp(toDateStr);
        return expensesRepo.getAll(page, expensesConfigId, truckLicense, fromDate, toDate);
    }

    @Override
    public List<ExpensesDTO> getAll(String expensesConfigId, String truckLicense, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKey.VIEW);
        Date fromDate = Utils.convertToDateOfTimestamp(fromDateStr);
        Date toDate = Utils.convertToDateOfTimestamp(toDateStr);
        return expensesRepo.getAll(expensesConfigId, truckLicense, fromDate, toDate);
    }

    @Override
    public ExpensesDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        return expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Chi phí không tồn tại!"));
    }

    @Override
    @Transactional
    public ExpensesDTO create(ExpensesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        String[] attachedImagePaths = dto.getAttachedPaths();
        Expenses expenses = expensesMapper.toExpenses(dto);
        attachedService.addAttachedImages(expenses.getId(), AttachedType.ATTACHED_OF_EXPENSES, attachedImagePaths);
        expensesRepo.save(expenses);

        String notifyMsg = "Có một chi phí được tạo mới cần được phê duyệt lúc " + new Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        return expensesRepo.getByID(expenses.getId()).orElse(null);
    }

    @Override
    public ExpensesDTO update(String id, ExpensesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        Expenses expenses = expensesRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi phí cần cập nhật không tồn tại!"));

        ExpensesStatus status = expensesRepo.getStatusByID(id);
        if (status == ExpensesStatus.APPROVED) {
            throw new ConflictException("Chi phí đã được duyệt không thể chỉnh sửa!");
        }

        expensesMapper.updateExpenses(expenses, dto);
        expensesRepo.save(expenses);

        Optional<ExpensesDTO> res = expensesRepo.getByID(expenses.getId());
        return res.orElse(null);
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.DELETE);
        if (expensesRepo.countByID(id) == 0)
            throw new NotFoundException("Chi phí cần xóa không tồn tại hoặc đã được xóa trước đó!");

        long numOfRowsDeleted = expensesRepo.delete(id);
        if (numOfRowsDeleted == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return numOfRowsDeleted;
    }

    @Override
    public long approveByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.APPROVE);

        if (expensesRepo.countByID(id) == 0)
            throw new NotFoundException("Chi phí cần duyệt không tồn tại!");

        ExpensesStatus status = expensesRepo.getStatusByID(id);
        if (status == ExpensesStatus.APPROVED) {
            throw new NotModifiedException("Chi phí đã được duyệt trước đó!");
        }

        long numOfRowsApproved = expensesRepo.approve(id);
        if (numOfRowsApproved == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return numOfRowsApproved;
    }

    @Override
    public List<ExpensesIncurredDTO> report(String driverId, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        Date fromDate = Utils.convertToDate(period);
        Date toDate = Utils.convertToDateOfNextMonth(period);
        return expensesRepo.getExpenseIncurredByDriverID(driverId, fromDate, toDate);
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
