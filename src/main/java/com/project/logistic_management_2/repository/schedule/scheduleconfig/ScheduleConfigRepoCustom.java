package com.project.logistic_management_2.repository.schedule.scheduleconfig;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;

import java.util.List;
import java.util.Optional;

public interface ScheduleConfigRepoCustom {
    List<ScheduleConfigDTO> getAll(int page);
    List<ScheduleConfigDTO> getAll();
    Optional<ScheduleConfigDTO> getByID(String id);
    long delete(String id);
    long countByID(String id);
}
