package com.project.logistic_management_2.service.schedule.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.enums.schedule.ScheduleType;
import com.project.logistic_management_2.enums.truck.TruckType;
import com.project.logistic_management_2.exception.def.ConflictException;
import com.project.logistic_management_2.exception.def.InvalidFieldException;
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

import java.rmi.ServerException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

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
                throw new InvalidFieldException("Cấu hình lịch trình không được để trống!");
            }
            if (scheduleDTO.getDepartureTime() == null) {
                throw new InvalidFieldException("Thời gian lấy hàng không được để trống!");
            } else {
                if (scheduleDTO.getDepartureTime().before(new java.util.Date())) {
                    throw new InvalidFieldException("Thời gian khởi hành không hợp lệ. Thời gian chỉ được tính sau thời điểm lịch trình được tạo!");
                }
            }
        }
        validateTruck(scheduleDTO.getTruckLicense(), TruckType.TRUCK_HEAD);
        validateTruck(scheduleDTO.getMoocLicense(), TruckType.MOOC);
    }

    private void validateTruck(String license, TruckType type) {
        String message = null;
        Integer typeNumberOfTruck = truckRepo.getTypeByLicensePlate(license);
        if (typeNumberOfTruck == null) {
            message = String.format("Xe tải có biển số %s không tồn tại!", license);
        } else if (!typeNumberOfTruck.equals(type.getValue())) {
            message = String.format("Loại xe đang chọn không hợp lệ. Vui lòng chọn %s!", type.getDescription());
        }
        if (message != null) {
            throw new InvalidFieldException(message);
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
        String notifyMsg = "Lịch trình mới được khởi tạo cần được phê duyệt lúc " + new java.util.Date();
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

        if (dto.getTruckLicense() != null) {
            validateTruck(dto.getTruckLicense(), TruckType.TRUCK_HEAD);
        }
        if (dto.getMoocLicense() != null) {
            validateTruck(dto.getMoocLicense(), TruckType.MOOC);
        }
        scheduleMapper.updateSchedule(schedule, dto);
        scheduleRepo.save(schedule);

        Optional<ScheduleDTO> res = scheduleRepo.getByID(id);
        return res.orElse(null);
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.DELETE);
        if (scheduleRepo.countByID(id) == 0) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại hoặc đã bị xóa!");
        }
        long numOfRowsDeleted = scheduleRepo.delete(id);
        if (numOfRowsDeleted == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return numOfRowsDeleted;
    }

    @Override
    public long approveByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.APPROVE);

        ScheduleStatus status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại!");
        } else if (status != ScheduleStatus.WAITING_FOR_APPROVAL) {
            return -1; //Thông báo đã duyệt
        }

        long numOfRowsApproved = scheduleRepo.approve(id);
        if (numOfRowsApproved == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return numOfRowsApproved;
    }

    @Override
    @Transactional
    public long markComplete(String id) throws ServerException {
        ScheduleStatus status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        }
        switch (status) {
            case ScheduleStatus.WAITING_FOR_APPROVAL, ScheduleStatus.NOT_APPROVED
                    -> throw new ConflictException("Lịch trình chưa/không được duyệt để di chuyển!");
            case ScheduleStatus.COMPLETED -> { return ScheduleStatus.COMPLETED.getValue(); }
        }

        long numOfRowsMarked = scheduleRepo.markComplete(id);
        if (numOfRowsMarked == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return scheduleRepo.markComplete(id);
    }

    @Override
    public List<ScheduleDTO> report(String license, int year, int month) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        Date fromDate = convertToDate(year, month);
        Date toDate = convertToDate(year, (month % 12) + 1);
        return scheduleRepo.exportReport(license, fromDate, toDate);
    }

    @Override
    public List<ScheduleSalaryDTO> exportScheduleSalary (String driverId, int year, int month) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        Date fromDate = convertToDate(year, month);
        Date toDate = convertToDate(year, (month % 12) + 1);
        return scheduleRepo.exportScheduleSalary(driverId, fromDate, toDate);
    }

    private Date convertToDate(int year, int month) {
        try {
            LocalDate localDate = LocalDate.of(year, month, 1);
            return Date.valueOf(localDate);
        } catch (DateTimeException ex) {
            throw new InvalidParameterException("Chu kỳ đã chọn không hợp lệ!");
        }
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
