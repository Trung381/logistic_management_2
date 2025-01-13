package com.project.logistic_management_2.repository.schedule.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepoCustom {
    List<ScheduleDTO> getAll(int page, String driverId, String truckLicense, Date fromDate, Date toDate);
    List<ScheduleDTO> getAll(String driverId, String truckLicense, Date fromDate, Date toDate);
    Optional<ScheduleDTO> getByID(String id);
    long delete(String id);
    long approve(String id, boolean approved);
    long markComplete(String id);
    List<ScheduleSalaryDTO> exportScheduleSalary(String driverId, Date fromDate, Date toDate);
    List<ScheduleDTO> exportReport(String license, Date fromDate, Date toDate);
    long countByID(String id);
    ScheduleStatus getStatusByID(String id);
    List<ScheduleDTO> findByLicensePlate(String licensePlate);
}
