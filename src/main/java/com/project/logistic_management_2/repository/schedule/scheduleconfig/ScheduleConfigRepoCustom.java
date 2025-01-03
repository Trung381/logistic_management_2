package com.project.logistic_management_2.repository.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;

import java.util.List;
import java.util.Optional;

public interface ScheduleConfigRepoCustom {
    List<ScheduleConfigDTO> getAll();
    Optional<ScheduleConfig> getByID(String id);
    long delete(String id);
    long countByID(String id);
}
