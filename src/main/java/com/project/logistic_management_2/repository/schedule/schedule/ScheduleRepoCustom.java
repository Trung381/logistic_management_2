package com.project.logistic_management_2.repository.schedule.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepoCustom {
    List<ScheduleDTO> getAll(int page, String driverId, String truckLicense, Timestamp fromDate, Timestamp toDate);
    List<ScheduleDTO> getAll(String driverId, String truckLicense, Timestamp fromDate, Timestamp toDate);
    Optional<ScheduleDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
    long markComplete(String id);
    List<ScheduleSalaryDTO> exportScheduleSalary(String driverId, Date fromDate, Date toDate);
    List<ScheduleDTO> exportReport(String license, Date fromDate, Date toDate);
    long countByID(String id);
    ScheduleStatus getStatusByID(String id);
}
