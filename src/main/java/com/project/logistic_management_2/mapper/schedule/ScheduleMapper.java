package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.enums.schedule.ScheduleType;
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
                .note(dto.getNote())
                .type(dto.getType().getValue())
                .status(ScheduleStatus.WAITING_FOR_APPROVAL.getValue())
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
                        .type(dto.getType().getValue())
                        .status(ScheduleStatus.WAITING_FOR_APPROVAL.getValue())
                        .deleted(false)
                        .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                        .updatedAt(new Date())
                        .build()
        ).collect(Collectors.toList());
    }

    public void updateSchedule(Schedule schedule, ScheduleDTO dto) {
        if (dto == null) return;

        if (dto.getScheduleConfigId() != null)
            schedule.setScheduleConfigId(dto.getScheduleConfigId());

        if (dto.getTruckLicense() != null)
            schedule.setTruckLicense(dto.getTruckLicense());

        if (dto.getMoocLicense() != null)
            schedule.setMoocLicense(dto.getMoocLicense());

        if (dto.getAttachDocument() != null)
            schedule.setAttachDocument(dto.getAttachDocument());

        if (dto.getDepartureTime() != null)
            schedule.setDepartureTime(dto.getDepartureTime());

        if (dto.getArrivalTime() != null)
            schedule.setArrivalTime(dto.getArrivalTime());

        if (dto.getNote() != null)
            schedule.setNote(dto.getNote());

        if (dto.getType() != null)
            schedule.setType(dto.getType().getValue());

        schedule.setUpdatedAt(new Date());
    }
}
