package com.project.logistic_management_2.service.expenses.expenses;

import com.mysema.commons.lang.Pair;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesReportDTO;
import com.project.logistic_management_2.entity.Expenses;
import com.project.logistic_management_2.enums.expenses.ExpensesStatus;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.def.ConflictException;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.exception.def.NotModifiedException;
import com.project.logistic_management_2.mapper.expenses.ExpensesMapper;
import com.project.logistic_management_2.repository.expenses.expenses.ExpensesRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.notification.NotificationService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.rmi.ServerException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.project.logistic_management_2.utils.Utils.parseAndValidateDates;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl extends BaseService implements ExpensesService {
    private final ExpensesRepo expensesRepo;
    private final ExpensesMapper expensesMapper;
    private final NotificationService notificationService;
    private final PermissionType type = PermissionType.EXPENSES;

    @Override
    public List<ExpensesDTO> getAll(int page, String expensesConfigId, String truckLicense, String fromDate, String toDate) {
        checkPermission(type, PermissionKey.VIEW);
        if (page <= 0) {
            throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
        }

        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        return expensesRepo.getAll(page, expensesConfigId, truckLicense, dateRange.getFirst(), dateRange.getSecond());
    }


    @Override
    public ExpensesDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        return expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Chi phí không tồn tại!"));
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
    public List<ExpensesIncurredDTO> report(String driverId, int year, int month) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        java.sql.Date fromDate = convertToDate(year, month);
        java.sql.Date toDate = convertToDate(year, (month % 12) + 1);
        return expensesRepo.getByFilter(driverId, fromDate, toDate);
    }

    @Override
    public List<ExpensesReportDTO> reportForAll(int year, int month) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        if (month < 1 || month > 12) {
            throw new InvalidParameterException("Chu kỳ đã chọn không hợp lệ!");
        }
        String period = year + "-" + month;
        return expensesRepo.reportForAll(period);
    }

    private java.sql.Date convertToDate(int year, int month) {
        try {
            LocalDate localDate = LocalDate.of(year, month, 1);
            return java.sql.Date.valueOf(localDate);
        } catch (DateTimeException ex) {
            throw new InvalidParameterException("Chu kỳ đã chọn không hợp lệ!");
        }
    }

    @Override
    public List<Expenses> importExpensesData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ExpensesDTO> expensesDTOList = ExcelUtils.getImportData(workbook, ImportConfig.expensesImport);

        List<Expenses> expenses = expensesMapper.toExpensesList(expensesDTOList);

        return expensesRepo.saveAll(expenses);
    }

    @Override
    public ExportExcelResponse exportExpenses(int page, String expensesConfigId, String truckLicense, String fromDate, String toDate) throws Exception {
        Pair<Timestamp, Timestamp> dateRange = parseAndValidateDates(fromDate, toDate);

        List<ExpensesDTO> expenses = expensesRepo.getAll(page, expensesConfigId, truckLicense, dateRange.getFirst(), dateRange.getSecond());

        if (CollectionUtils.isEmpty(expenses)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Expenses Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(expenses, fileName, ExportConfig.expensesExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    @Override
    public ExportExcelResponse exportReportExpenses(String driverId, int year, int month) throws Exception {

        java.sql.Date fromDate = convertToDate(year, month);
        java.sql.Date toDate = convertToDate(year, (month % 12) + 1);
        List<ExpensesIncurredDTO> expensesReport = expensesRepo.getByFilter(driverId, fromDate, toDate);


        if (CollectionUtils.isEmpty(expensesReport)) {
            throw new NotFoundException("No data");
        }
        String fileName = "ExpensesReport Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(expensesReport, fileName, ExportConfig.expenseReportByDriverExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
