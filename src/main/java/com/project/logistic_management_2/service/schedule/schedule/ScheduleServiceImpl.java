package com.project.logistic_management_2.service.schedule.schedule;

import com.project.logistic_management_2.dto.attached.AttachedImagePathsDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.enums.attached.AttachedType;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.enums.schedule.ScheduleType;
import com.project.logistic_management_2.enums.truck.TruckStatus;
import com.project.logistic_management_2.enums.truck.TruckType;
import com.project.logistic_management_2.exception.define.*;
import com.project.logistic_management_2.mapper.schedule.ScheduleMapper;
import com.project.logistic_management_2.repository.schedule.schedule.ScheduleRepo;
import com.project.logistic_management_2.repository.truck.TruckRepo;
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
public class ScheduleServiceImpl extends BaseService implements ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final TruckRepo truckRepo;
    private final ScheduleMapper scheduleMapper;
    private final NotificationService notificationService;
    private final AttachedImageService attachedService;
    private final PermissionType type = PermissionType.SCHEDULES;

    private void validate(ScheduleDTO dto) {
        if (dto.getType() == ScheduleType.PAYROLL) {
            validatePayrollSchedule(dto);
        }
        validateTruck(dto.getTruckLicense(), TruckType.TRUCK_HEAD);
        validateTruck(dto.getMoocLicense(), TruckType.MOOC);
    }

    private void validatePayrollSchedule(ScheduleDTO dto) {
        if (dto.getScheduleConfigId().isBlank()) {
            throw new InvalidFieldException("Cấu hình lịch trình không được để trống!");
        }
        if (dto.getDepartureTime() == null) {
            throw new InvalidFieldException("Thời gian lấy hàng không được để trống!");
        } else {
            if (dto.getDepartureTime().before(new Date())) {
                throw new InvalidFieldException("Thời gian khởi hành không hợp lệ. Thời gian chỉ được tính sau thời điểm lịch trình được tạo!");
            }
        }
    }

    private void validateTruck(String license, TruckType type) {
        String message = null;
        Truck truck = truckRepo.findByLicensePlate(license)
                .orElseThrow(() -> new NotFoundException("Xe có biển số " + license + " không tồn tại!"));
        if (!truck.getType().equals(type.getValue())) {
            message = String.format("Loại xe đang chọn không hợp lệ. Vui lòng chọn %s!", type.getDescription());
        } else if (!truck.getStatus().equals(TruckStatus.AVAILABLE.getValue())) { //Check trang thai xe neu sung loai xe
            message = type.getDescription() + " được chọn không có sẵn để lên lịch. Vui lòng chọn xe khác!";
        }
        if (message != null) {
            throw new InvalidFieldException(message);
        }
    }

    @Override
    public List<ScheduleDTO> getAll(Integer page, String driverId, String truckLicense, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(fromDateStr, toDateStr);
        if (page != null) {
            if (page <= 0) {
                throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
            } else {
                return scheduleRepo.getAll(page, driverId, truckLicense, range[0], range[1]);
            }
        } else {
            return scheduleRepo.getAll(driverId, truckLicense, range[0], range[1]);
        }
    }

    @Override
    public ScheduleDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        return scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Lịch trình cần tìm không tồn tại!"));
    }

    @Override
    @Transactional
    public ScheduleDTO create(ScheduleDTO dto) throws ServerException {
        checkPermission(type, PermissionKey.WRITE);
        validate(dto);
        Schedule schedule = createScheduleFromDTO(dto);

        // Gửi notification qua WebSocket
        String notifyMsg = "Lịch trình mới được khởi tạo cần được phê duyệt lúc " + new java.util.Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        Optional<ScheduleDTO> result = scheduleRepo.getByID(schedule.getId());
        if (result.isEmpty()) {
            throw new ServerException("Có lỗi khi tạo lịch trình mới. Vui lòng thử lại sau!");
        }
        return result.get();
    }

    private Schedule createScheduleFromDTO(ScheduleDTO dto) {
        Schedule schedule = scheduleMapper.toSchedule(dto);
        scheduleRepo.save(schedule);
        return schedule;
    }

    @Override
    public ScheduleDTO update(String id, ScheduleDTO dto) {
        checkPermission(type, PermissionKey.WRITE);

        Schedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Lịch trình cần cập nhật không tồn tại!"));
        checkUpdateConditions(schedule);

        updateScheduleFromDTO(schedule, dto);

        Optional<ScheduleDTO> result = scheduleRepo.getByID(id);
        return result.orElse(null);
    }

    private void updateScheduleFromDTO(Schedule schedule, ScheduleDTO dto) {
        scheduleMapper.updateSchedule(schedule, dto);
        if (dto.getTruckLicense() != null) {
            validateTruck(dto.getTruckLicense(), TruckType.TRUCK_HEAD);
        }
        if (dto.getMoocLicense() != null) {
            validateTruck(dto.getMoocLicense(), TruckType.MOOC);
        }
        scheduleRepo.save(schedule);
    }

    private void checkUpdateConditions(Schedule schedule) {
        //Chỉ sửa được trước ngày bắt đầu (departure time) hoặc chưa duyệt
        Date currentTime = new Date();
        if (
                ScheduleStatus.valueOf(schedule.getStatus()) != ScheduleStatus.PENDING
                        || (schedule.getDepartureTime() != null && schedule.getDepartureTime().before(currentTime))) {
            throw new ConflictException("Lịch trình đã hết thời gian được phép chỉnh sửa!");
        }
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKey.DELETE);
        if (scheduleRepo.countByID(id) == 0) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại!");
        }
        long numOfRowsDeleted = scheduleRepo.delete(id);
        if (numOfRowsDeleted == 0) {
            throw new ServerException("Có lỗi khi xóa lịch trình. Vui lòng thử lại sau!");
        }
        return numOfRowsDeleted;
    }

    @Override
    public long approveByID(String id, boolean approved) throws ServerException {
        checkPermission(type, PermissionKey.APPROVE);
        checkApproveConditions(id);
        return approveSchedule(id, approved);
    }

    private void checkApproveConditions(String id) {
        ScheduleStatus status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        } else if (status != ScheduleStatus.PENDING) {
            throw new NotModifiedException("Lịch trình đã được xử lý trước đó!");
        }
    }

    private long approveSchedule(String id, boolean approved) throws ServerException {
        long result = scheduleRepo.approve(id, approved);
        if (result == 0) {
            throw new ServerException("Có lỗi khi duyệt lịch trình. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    @Transactional
    public long markComplete(String id, AttachedImagePathsDTO attachedImagePathsDTO) throws ServerException {
        checkMarkCompleteConditions(id);
        attachedService.addAttachedImages(id, AttachedType.ATTACHED_OF_SCHEDULE, attachedImagePathsDTO);
        return mark(id);
    }

    private void checkMarkCompleteConditions(String id) {
        ScheduleStatus status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        }
        switch (status) {
            case ScheduleStatus.PENDING, ScheduleStatus.REJECTED ->
                    throw new ConflictException("Lịch trình chưa/không được duyệt để di chuyển!");
            case ScheduleStatus.COMPLETED ->
                    throw new NotModifiedException("Chuyến đi đã được đánh dấu là hoàn thành trước đó!");
        }
    }

    private long mark(String id) throws ServerException {
        long result = scheduleRepo.markComplete(id);
        if (result == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    public List<ScheduleDTO> report(String license, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        return scheduleRepo.exportReport(license, range[0], range[1]);
    }

    @Override
    public List<ScheduleSalaryDTO> exportScheduleSalary (String driverId, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        return scheduleRepo.exportScheduleSalary(driverId, range[0], range[1]);
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
