package com.project.logistic_management_2.service.schedule.schedule;

import com.project.logistic_management_2.dto.attached.AttachedImagePathsDTO;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.Schedule;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAll(Integer page, String driverId, String truckLicense, String fromDateStr, String toDateStr);
    ScheduleDTO getByID(String id);
    ScheduleDTO create(ScheduleDTO dto) throws ServerException;
    ScheduleDTO update(String id, ScheduleDTO dto);
    long deleteByID(String id) throws ServerException;
    long approveByID(String id, boolean approved) throws ServerException;
    long markComplete(String id, AttachedImagePathsDTO attachedImagePathsDTO) throws ServerException;
    List<ScheduleDTO> report(String license, String period);
    List<ScheduleSalaryDTO> exportScheduleSalary (String driverId, String period);
    List<Schedule> importScheduleData(MultipartFile importFile);
    ExportExcelResponse exportSchedule(String driverId, String truckLicense, String fromDate, String toDate) throws Exception;
}
