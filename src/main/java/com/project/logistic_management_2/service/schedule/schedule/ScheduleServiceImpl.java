package com.project.logistic_management_2.service.schedule.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
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
import jakarta.validation.Validator;
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

    /**
     *
     * @param dto: object needs validation
     * @param <T>: Object type of dto
     */
    private <T> void validateDTO(T dto) {
        if (!(dto instanceof ScheduleDTO scheduleDTO)) {
            return;
        }
        if (scheduleDTO.getType() == 1) {
            if (scheduleDTO.getScheduleConfigId().isBlank()) {
                throw new InvalidParameterException("Cấu hình lịch trình không được để trống!");
            }
            if (scheduleDTO.getDepartureTime() == null) {
                throw new InvalidParameterException("Thời gian lấy hàng không được để trống!");
            }
            if (scheduleDTO.getArrivalTime() == null) {
                throw new InvalidParameterException("Thời gian giao hàng không được để trống!");
            }
        }
        if (truckRepo.getTruckByLicensePlate(scheduleDTO.getTruckLicense()).isEmpty()) {
            throw new InvalidParameterException("Xe tải có biển số " + scheduleDTO.getTruckLicense() + " không tồn tại!");
        }
        if (truckRepo.getTruckByLicensePlate(scheduleDTO.getMoocLicense()).isEmpty()) {
            throw new InvalidParameterException("Rơ-mooc có biển số " + scheduleDTO.getMoocLicense() + " không tồn tại!");
        }
    }

    @Override
    public List<ScheduleDTO> getAll(String driverId, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        checkPermission(type, PermissionKey.VIEW);
        return scheduleRepo.getAll(driverId, truckLicense, fromDate, toDate);
    }

    @Override
    public ScheduleDTO getByID(String id) {
        checkPermission(type, PermissionKey.VIEW);
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin lịch trình!"));
    }

    @Override
    @Transactional
    public ScheduleDTO create(ScheduleDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        validateDTO(dto);
        //Cập nhật trạng thái xe
        //đầu xe + mooc

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
        if (schedule.getStatus() != 0 || (schedule.getDepartureTime() != null && schedule.getDepartureTime().before(currentTime))) {
            throw new ConflictException("Lịch trình đã hết thời gian được phép chỉnh sửa!");
        }

        // Update object
        scheduleMapper.updateSchedule(schedule, dto);
        // Save to DB
        scheduleRepo.save(schedule);

        Optional<ScheduleDTO> res = scheduleRepo.getByID(id);
        return res.orElse(null);
    }

    @Override
    public long deleteByID(String id) {
        checkPermission(type, PermissionKey.DELETE);
        if (scheduleRepo.countByID(id) == 0) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại hoặc đã được xóa trước đó!");
        }
        return scheduleRepo.delete(id);
    }

    @Override
    public long approveByID(String id) {
        checkPermission(type, PermissionKey.APPROVE);
        // Trạng thái lịch trình: -1 - Không duyệt, 0 - Đang chờ, 1 - Đã duyệt và chưa hoàn thành, 2 - Đã hoàn thành
//        if (scheduleRepo.countByID(id) == 0) {
//            throw new NotFoundException("Lịch trình cần duyệt không tồn tại!");
//        }

        Optional<Integer> status = scheduleRepo.getStatusByID(id);
        if (status.isEmpty()) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại!");
        } else if (status.get() != 0) {
            return -1; //Thông báo đã duyệt
        }

        return scheduleRepo.approve(id);
    }

    @Override
    @Transactional
    public long markComplete(String id) {
        //-1 - Không duyệt, 0 - Đang chờ, 1 - Đã duyệt và chưa hoàn thành, 2 - Đã hoàn thành
        Optional<Integer> status = scheduleRepo.getStatusByID(id);
        if (status.isEmpty()) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        }
        switch (status.get()) {
            case 0, -1 -> throw new ConflictException("Lịch trình chưa/không được duyệt để di chuyển!"); //Lịch trình chưa/không được duyệt
            case 2 -> { return 2; } //Đã duyệt
        }

        return scheduleRepo.markComplete(id);
    }

    //Báo cáo lịch trình theo xe: biển số xe, tháng nào
    @Override
    public List<ScheduleDTO> report(String license, String period) {
        checkPermission(PermissionType.REPORTS, PermissionKey.VIEW);
        YearMonth periodYM = parsePeriod(period);
//        return scheduleRepo.getByFilter(license, periodYM);

        //Báo cáo theo fe (Có cột số chuyến phát sinh)
        return scheduleRepo.exportReport(license, periodYM);
    }

    //Xuất lương lịch trình của một tài xế trong một chu kỳ (tháng)
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

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return scheduleRepo.saveAll(schedule);
    }
}
