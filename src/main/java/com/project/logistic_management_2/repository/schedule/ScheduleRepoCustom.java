package com.project.logistic_management_2.repository.schedule;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepoCustom {
    List<ScheduleDTO> getAll();
    Optional<ScheduleDTO> getByID(String id);
    long delete(String id);
    long approve(String id);
}
