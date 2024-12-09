package com.project.logistic_management_2.service.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAll();
    ScheduleDTO getByID(String id);
    ScheduleDTO create(ScheduleDTO dto);
    ScheduleDTO update(String id, ScheduleDTO dto);
    long deleteByID(String id);
    long approveByID(String id);
    long markComplete(String id);
    List<ScheduleDTO> report(String license, String period);
    List<ScheduleSalaryDTO> exportScheduleSalary (String driverId, String period);
}
