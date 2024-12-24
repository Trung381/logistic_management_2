package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleMapper {
    public Schedule toSchedule(ScheduleDTO dto) {
        if (dto == null) return null;
        return Schedule.builder()
                .id(Utils.genID(IDKey.SCHEDULE))
                .scheduleConfigId(dto.getScheduleConfigId().isBlank() ? null : dto.getScheduleConfigId())
                .truckLicense(dto.getTruckLicense())
                .moocLicense(dto.getMoocLicense())
                .attachDocument(dto.getAttachDocument())
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getDepartureTime())
                .note(dto.getNote())
                .type(dto.getType())
                .status(0)
                .deleted(false)
                .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                .updatedAt(new Date())
                .build();
    }

    public List<Schedule> toScheduleList(List<ScheduleDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(dto ->
                Schedule.builder()
                        .id(Utils.genID(IDKey.SCHEDULE))
                        .scheduleConfigId(dto.getScheduleConfigId().isBlank() ? null : dto.getScheduleConfigId())
                        .truckLicense(dto.getTruckLicense())
                        .moocLicense(dto.getMoocLicense())
                        .attachDocument(dto.getAttachDocument())
                        .departureTime(dto.getDepartureTime())
                        .arrivalTime(dto.getDepartureTime())
                        .note(dto.getNote())
                        .type(dto.getType())
                        .status(0)
                        .deleted(false)
                        .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                        .updatedAt(new Date())
                        .build()
        ).collect(Collectors.toList());
    }

    public void updateSchedule(String id, Schedule schedule, ScheduleDTO dto) {
        if (dto == null) return;
        schedule.setId(id);
        schedule.setScheduleConfigId(dto.getScheduleConfigId());
        schedule.setTruckLicense(dto.getTruckLicense());
        schedule.setMoocLicense(dto.getMoocLicense());
        schedule.setAttachDocument(dto.getAttachDocument());
        schedule.setDepartureTime(dto.getDepartureTime());
        schedule.setArrivalTime(dto.getArrivalTime());
        schedule.setNote(dto.getNote());
        schedule.setType(dto.getType());
        schedule.setUpdatedAt(new Date());
    }
}
