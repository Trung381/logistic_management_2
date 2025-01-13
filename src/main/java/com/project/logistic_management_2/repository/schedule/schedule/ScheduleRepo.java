package com.project.logistic_management_2.repository.schedule.schedule;

import com.project.logistic_management_2.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, String>, ScheduleRepoCustom {
    @Override
    Optional<Schedule> findById(@NonNull String id);
}
