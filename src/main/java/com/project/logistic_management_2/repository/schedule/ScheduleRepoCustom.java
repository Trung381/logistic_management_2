package com.project.logistic_management_2.repository.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepoCustom {
    List<ScheduleDTO> getAll(String driverId, String truckLicense, Timestamp fromDate, Timestamp toDate);
    List<ScheduleDTO> getByFilter(String license, YearMonth period);
    Optional<ScheduleDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
    long markComplete(String id);
    List<ScheduleSalaryDTO> exportScheduleSalary(String driverId, YearMonth period);
    List<ScheduleDTO> exportReport(String license, YearMonth period);
}
