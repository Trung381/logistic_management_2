package com.project.logistic_management_2.service.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScheduleConfigService {
    List<ScheduleConfigDTO> getAll();
    ScheduleConfigDTO getByID(String id);
    ScheduleConfigDTO create(ScheduleConfigDTO dto);
    ScheduleConfigDTO update(String id, ScheduleConfigDTO dto);
    long deleteByID(String id);
    List<ScheduleConfig> importScheduleConfigData(MultipartFile importFile);

}
