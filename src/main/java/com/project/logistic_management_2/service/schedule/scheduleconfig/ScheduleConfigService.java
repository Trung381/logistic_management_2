package com.project.logistic_management_2.service.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.List;

public interface ScheduleConfigService {
    List<ScheduleConfigDTO> getAll(int page);
    List<ScheduleConfigDTO> getAll();
    ScheduleConfigDTO getByID(String id);
    ScheduleConfigDTO create(ScheduleConfigDTO dto);
    ScheduleConfigDTO update(String id, ScheduleConfigDTO dto);
    long deleteByID(String id) throws ServerException;
    List<ScheduleConfig> importScheduleConfigData(MultipartFile importFile);
}
