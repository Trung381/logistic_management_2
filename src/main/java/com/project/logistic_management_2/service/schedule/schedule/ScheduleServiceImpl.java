package com.project.logistic_management_2.service.schedule.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.enums.schedule.ScheduleType;
import com.project.logistic_management_2.exception.def.ConflictException;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.schedule.ScheduleMapper;
import com.project.logistic_management_2.repository.schedule.schedule.ScheduleRepo;
import com.project.logistic_management_2.repository.truck.TruckRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.service.notification.NotificationService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import jakarta.transaction.Transactional;
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
public class ScheduleServiceImpl extends BaseService implements ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final TruckRepo truckRepo;
    private final ScheduleMapper scheduleMapper;
    private final NotificationService notificationService;
    private final PermissionType type = PermissionType.SCHEDULES;

    private <T> void validateDTO(T dto) {
        if (!(dto instanceof ScheduleDTO scheduleDTO)) {
            return;
        }
        if (scheduleDTO.getType() == ScheduleType.PAYROLL) {
            if (scheduleDTO.getScheduleConfigId().isBlank()) {
                throw new InvalidParameterException("Cấu hình lịch trình không được để trống!");
            }
            if (scheduleDTO.getDepartureTime() == null) {
                throw new InvalidParameterException("Thời gian lấy hàng không được để trống!");
            } else {
                if (scheduleDTO.getDepartureTime().before(new Date())) {
                    throw new InvalidParameterException("Thời gian khởi hành không hợp lệ. Thời gian chỉ được tính sau thời điểm lịch trình được tạo!");
                }
            }
        }
        if (truckRepo.getTruckByLicensePlate(scheduleDTO.getTruckLicense()).isEmpty()) {
            throw new InvalidParameterException("Xe tải có biển số " + scheduleDTO.getTruckLicense() + " không tồn tại!");
        }
        if (truckRepo.getTruckByLicensePlate(scheduleDTO.getMoocLicense()).isEmpty()) {
            throw new InvalidParameterException("Rơ-mooc có biển số " + scheduleDTO.getMoocLicense() + " không tồn tại!");
        }
    }

    // Pagination
    @Override
    public List<ScheduleDTO> getAll(int page, String driverId, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        checkPermission(type, PermissionKey.VIEW);
        if (page <= 0) {
            throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
        }
        return scheduleRepo.getAll(page, driverId, truckLicense, fromDate, toDate);
    }

    @Override
    public List<ScheduleDTO> getAll(String driverId, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        checkPermission(type, PermissionKey.VIEW);
        return scheduleRepo.getAll(driverId, truckLicense, fromDate, toDate);
    }

    @Override
    public ScheduleDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        return scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Lịch trình cần tìm không tồn tại!"));
    }

    @Override
    @Transactional
    public ScheduleDTO create(ScheduleDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        validateDTO(dto);

        Schedule schedule = scheduleMapper.toSchedule(dto);
        scheduleRepo.save(schedule);

        // Gửi notification qua WebSocket
        String notifyMsg = "Lịch trình mới được khởi tạo cần được phê duyệt lúc " + new Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        return scheduleRepo.getByID(schedule.getId()).get();
    }

    @Override
    public ScheduleDTO update(String id, ScheduleDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        Schedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Lịch trình cần cập nhật không tồn tại!"));

        //Chỉ sửa được trước ngày bắt đầu (departure time) hoặc chưa duyệt
        Date currentTime = new Date(System.currentTimeMillis());
        if (
                ScheduleStatus.valueOf(schedule.getStatus()) != ScheduleStatus.WAITING_FOR_APPROVAL
                        || (schedule.getDepartureTime() != null && schedule.getDepartureTime().before(currentTime))) {
            throw new ConflictException("Lịch trình đã hết thời gian được phép chỉnh sửa!");
        }

        scheduleMapper.updateSchedule(schedule, dto);
        scheduleRepo.save(schedule);

        Optional<ScheduleDTO> res = scheduleRepo.getByID(id);
        return res.orElse(null);
    }

    @Override
    public long deleteByID(String id) {
        checkPermission(type, PermissionKey.DELETE);
        if (scheduleRepo.countByID(id) == 0) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại hoặc đã bị xóa trước đó!");
        }
        return scheduleRepo.delete(id);
    }

    @Override
    public long approveByID(String id) {
        checkPermission(type, PermissionKey.APPROVE);

        ScheduleStatus status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại!");
        } else if (status != ScheduleStatus.WAITING_FOR_APPROVAL) {
            return -1; //Thông báo đã duyệt
        }

        return scheduleRepo.approve(id);
    }

    @Override
    @Transactional
    public long markComplete(String id) {
        ScheduleStatus status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        }
        switch (status) {
            case ScheduleStatus.WAITING_FOR_APPROVAL, ScheduleStatus.NOT_APPROVED -> throw new ConflictException("Lịch trình chưa/không được duyệt để di chuyển!");
            case ScheduleStatus.COMPLETED -> { return 2; } //Đã hoàn thành
        }

        return scheduleRepo.markComplete(id);
    }

    @Override
    public List<ScheduleDTO> report(String license, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        YearMonth periodYM = parsePeriod(period);

        //Báo cáo theo fe (Có cột số chuyến phát sinh)
        return scheduleRepo.exportReport(license, periodYM);
    }

    @Override
    public List<ScheduleSalaryDTO> exportScheduleSalary (String driverId, String period) {
        YearMonth periodYM = parsePeriod(period);
        return scheduleRepo.exportScheduleSalary(driverId, periodYM);
    }

    private YearMonth parsePeriod(String period) {
        //Check định dạng: yyyy-MM
        String regex = "^(\\d{4}-(0[1-9]|1[0-2]))$";
        if (!Pattern.matches(regex, period)) {
            throw new InvalidParameterException("Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM");
        }
        return YearMonth.parse(period);
    }

    @Override
    public List<Schedule> importScheduleData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ScheduleDTO> scheduleDTOList = ExcelUtils.getImportData(workbook, ImportConfig.scheduleImport);

        List<Schedule> schedule = scheduleMapper.toScheduleList(scheduleDTOList);

        return scheduleRepo.saveAll(schedule);
    }
}
