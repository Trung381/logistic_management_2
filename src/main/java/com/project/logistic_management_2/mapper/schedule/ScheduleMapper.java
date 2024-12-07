package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleMapper {
    public Schedule toSchedule(ScheduleDTO dto) {
        if (dto == null) return null;
        return Schedule.builder()
                .id(Utils.genID(IDKey.SCHEDULE))
                .scheduleConfigId(dto.getScheduleConfigId())
                .truckLicense(dto.getTruckLicense())
                .moocLicense(dto.getMoocLicense())
                .attachDocument(dto.getAttachDocument())
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getDepartureTime())
                .note(dto.getNote())
                .status(0)
                .deleted(false)
                .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                .updatedAt(new Date())
                .build();
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
        schedule.setUpdatedAt(new Date());
    }
}
