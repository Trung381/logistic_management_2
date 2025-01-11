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
    public List<ExpensesDTO> getAll(Integer page, String expensesConfigId, String truckLicense, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(fromDateStr, toDateStr);
        if (page != null) {
            if (page <= 0) {
                throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
            } else {
                return expensesRepo.getAll(page, expensesConfigId, truckLicense, range[0], range[1]);
            }
        } else {
            return expensesRepo.getAll(expensesConfigId, truckLicense, range[0], range[1]);
        }
    }

    @Override
    public ExpensesDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        return expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Chi phí không tồn tại!"));
    }

    @Override
    @Transactional
    public ExpensesDTO create(ExpensesDTO dto) throws ServerException {
        checkPermission(type, PermissionKey.WRITE);

        Expenses expenses = createExpensesFromDTO(dto);
        attachImages(expenses.getId(), dto);

        String notifyMsg = "Có một chi phí được tạo mới cần được phê duyệt lúc " + new Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        Optional<ExpensesDTO> result = expensesRepo.getByID(expenses.getId());
        if (result.isEmpty()) {
            throw new ServerException("Có lỗi khi tạo chi phí mới. Vui lòng thử lại sau!");
        }
        return result.get();
    }

    private void attachImages(String referenceID, ExpensesDTO dto) {
        String[] attachedImagePaths = dto.getAttachedPaths();
        attachedService.addAttachedImages(referenceID, AttachedType.ATTACHED_OF_EXPENSES, attachedImagePaths);
    }

    private Expenses createExpensesFromDTO(ExpensesDTO dto) {
        Expenses expenses = expensesMapper.toExpenses(dto);
        expensesRepo.save(expenses);
        return expenses;
    }

    @Override
    public ExpensesDTO update(String id, ExpensesDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        Expenses expenses = expensesRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi phí cần cập nhật không tồn tại!"));
        checkUpdateConditions(id);

        updateExpensesFromDTO(expenses, dto);

        Optional<ExpensesDTO> result = expensesRepo.getByID(expenses.getId());
        return result.orElse(null);
    }

    private void checkUpdateConditions(String id) {
        ExpensesStatus status = expensesRepo.getStatusByID(id);
        if (status == ExpensesStatus.APPROVED) {
            throw new ConflictException("Chi phí đã được duyệt không thể chỉnh sửa!");
        }
    }

    private void updateExpensesFromDTO(Expenses expenses, ExpensesDTO dto) {
        expensesMapper.updateExpenses(expenses, dto);
        expensesRepo.save(expenses);
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.DELETE);
        checkDeleteConditions(id);
        return delete(id);
    }

    private void checkDeleteConditions(String id) {
        if (expensesRepo.countByID(id) == 0)
            throw new NotFoundException("Chi phí cần xóa không tồn tại!");
    }

    private long delete(String id) throws ServerException {
        long result = expensesRepo.delete(id);
        if (result == 0) {
            throw new ServerException("Có lỗi khi xóa chi phí. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    public long approveByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.APPROVE);
        checkApproveConditions(id);
        return approve(id);
    }

    private void checkApproveConditions(String id) {
        if (expensesRepo.countByID(id) == 0)
            throw new NotFoundException("Chi phí cần duyệt không tồn tại!");

        ExpensesStatus status = expensesRepo.getStatusByID(id);
        if (status == ExpensesStatus.APPROVED) {
            throw new NotModifiedException("Chi phí đã được duyệt trước đó!");
        }
    }

    private long approve(String id) throws ServerException {
        long result = expensesRepo.approve(id);
        if (result == 0) {
            throw new ServerException("Có lỗi khi duyệt chi phí. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    public List<ExpensesIncurredDTO> report(String driverId, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        return expensesRepo.getExpenseIncurredByDriverID(driverId, range[0], range[1]);
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
