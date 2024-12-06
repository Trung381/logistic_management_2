package com.project.logistic_management_2.repository.schedule;

import com.project.logistic_management_2.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, String>, ScheduleRepoCustom {
}
