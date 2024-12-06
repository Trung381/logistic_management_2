package com.project.logistic_management_2.service.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.exception.def.InvalidParameterException;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.schedule.ScheduleMapper;
import com.project.logistic_management_2.repository.schedule.ScheduleRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl extends BaseService implements ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final ScheduleMapper scheduleMapper;


    @Override
    public List<ScheduleDTO> getAll() {
        return scheduleRepo.getAll();
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
    public ScheduleDTO create(ScheduleDTO dto) {
        Schedule schedule = scheduleMapper.toSchedule(dto);
        scheduleRepo.save(schedule);
        return scheduleRepo.getByID(schedule.getId()).get();
    }

    @Override
    public ScheduleDTO update(String id, ScheduleDTO dto) {
        if (id == null || id.isEmpty()) {
            throw new InvalidParameterException("Tham số không hợp lệ!");
        }

        ScheduleDTO scheduleDTO = scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin lịch trình!"));

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
}
