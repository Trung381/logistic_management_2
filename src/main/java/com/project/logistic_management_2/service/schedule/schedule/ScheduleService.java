package com.project.logistic_management_2.service.schedule.schedule;

import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAll(int page, String driverId, String truckLicense, String fromDate, String toDate);
    ScheduleDTO getByID(String id);
    ScheduleDTO create(ScheduleDTO dto);
    ScheduleDTO update(String id, ScheduleDTO dto);
    long deleteByID(String id) throws ServerException;
    long approveByID(String id, boolean approved) throws ServerException;
    long markComplete(String id) throws ServerException;
    List<ScheduleDTO> report(String license, int year, int month);
    List<ScheduleSalaryDTO> exportScheduleSalary (String driverId, int year, int month);
    List<Schedule> importScheduleData(MultipartFile importFile);
    ExportExcelResponse exportSchedule(int page, String driverId, String truckLicense, String fromDate, String toDate) throws Exception;

}
