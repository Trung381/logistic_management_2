package com.project.logistic_management_2.repository.schedule.scheduleconfig;

import com.project.logistic_management_2.entity.schedule.ScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleConfigRepo extends JpaRepository<ScheduleConfig, String>, ScheduleConfigRepoCustom {
    Optional<ScheduleConfig> findById(String id);
}
