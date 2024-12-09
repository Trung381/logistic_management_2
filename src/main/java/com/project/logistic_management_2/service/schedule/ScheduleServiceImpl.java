package com.project.logistic_management_2.service.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.exception.def.ConflictException;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.schedule.ScheduleMapper;
import com.project.logistic_management_2.repository.schedule.ScheduleRepo;
import com.project.logistic_management_2.repository.truck.TruckRepo;
import com.project.logistic_management_2.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl extends BaseService implements ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final TruckRepo truckRepo;
    private final ScheduleMapper scheduleMapper;


    @Override
    public List<ScheduleDTO> getAll() {
        return scheduleRepo.getAll(null, null);
    }

    @Override
    public ScheduleDTO getByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin lịch trình!"));
    }

    @Override
    @Transactional
    public ScheduleDTO create(ScheduleDTO dto) {
        //Cập nhật trạng thái xe
        //đầu xe + mooc

        Schedule schedule = scheduleMapper.toSchedule(dto);
        scheduleRepo.save(schedule);
        truckRepo.updateStatus(dto.getTruckLicense(), 0);
        truckRepo.updateStatus(dto.getMoocLicense(), 0);
        return scheduleRepo.getByID(schedule.getId()).get();
    }

    @Override
    public ScheduleDTO update(String id, ScheduleDTO dto) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        ScheduleDTO scheduleDTO = scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin lịch trình!"));

        //Chỉ sửa được trước ngày bắt đầu (departure time) hoặc chưa duyệt
        Date currentTime = new Date(System.currentTimeMillis());
        if (scheduleDTO.getStatus() != 0 || (scheduleDTO.getDepartureTime() != null && scheduleDTO.getDepartureTime().before(currentTime))) {
            throw new ConflictException("Lịch trình này không thể chỉnh sửa!");
        }

        Schedule schedule = scheduleMapper.toSchedule(scheduleDTO);
        scheduleMapper.updateSchedule(id, schedule, dto);

        scheduleRepo.save(schedule);

        return scheduleRepo.getByID(schedule.getId()).get();
    }

    @Override
    public long deleteByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return scheduleRepo.delete(id);
    }

    @Override
    public long approveByID(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        return scheduleRepo.approve(id);
    }

    @Override
    @Transactional
    public long markComplete(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }
        long res = scheduleRepo.markComplete(id);
        if (res != 0) {
            Schedule schedule = scheduleRepo.findById(id).get();
            truckRepo.updateStatus(schedule.getTruckLicense(), 1);
            truckRepo.updateStatus(schedule.getMoocLicense(), 1);
        }
        return res;
    }

    //Báo cáo lịch trình theo xe: biển số xe, tháng nào
    @Override
    public List<ScheduleDTO> report(String license, String period) {
        YearMonth periodYM = parsePeriod(period);
//        return scheduleRepo.getAll(license, periodYM);

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
}
