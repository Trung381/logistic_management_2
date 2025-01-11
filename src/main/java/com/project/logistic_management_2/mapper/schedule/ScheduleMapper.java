package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.exception.define.InvalidFieldException;
import com.project.logistic_management_2.exception.define.NotModifiedException;
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
//                .attachDocument(dto.getAttachDocument())
                .departureTime(dto.getDepartureTime())
                .note(dto.getNote())
                .type(dto.getType().getValue())
                .status(ScheduleStatus.PENDING.getValue())
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
//                        .attachDocument(dto.getAttachDocument())
                        .departureTime(dto.getDepartureTime())
                        .arrivalTime(dto.getDepartureTime())
                        .note(dto.getNote())
                        .type(dto.getType().getValue())
                        .status(ScheduleStatus.PENDING.getValue())
                        .deleted(false)
                        .createdAt(dto.getCreatedAt() == null ? new Date() : dto.getCreatedAt())
                        .updatedAt(new Date())
                        .build()
        ).collect(Collectors.toList());
    }

    public void updateSchedule(Schedule schedule, ScheduleDTO dto) {
        if (dto == null) return;
        boolean isUpdated = false, isValidField = false;

        if (dto.getScheduleConfigId() != null) {
            if (!schedule.getScheduleConfigId().equals(dto.getScheduleConfigId())) {
                schedule.setScheduleConfigId(dto.getScheduleConfigId());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getTruckLicense() != null) {
            if (!schedule.getTruckLicense().equals(dto.getTruckLicense())) {
                schedule.setTruckLicense(dto.getTruckLicense());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getMoocLicense() != null) {
            if (!schedule.getMoocLicense().equals(dto.getMoocLicense())) {
                schedule.setMoocLicense(dto.getMoocLicense());
                isUpdated = true;
            }
            isValidField = true;
        }
//        if (dto.getAttachDocument() != null) {
//            if (!schedule.getAttachDocument().equals(dto.getAttachDocument())) {
//                schedule.setAttachDocument(dto.getAttachDocument());
//                isUpdated = true;
//            }
//            isValidField = true;
//        }
        if (dto.getDepartureTime() != null) {
            if (!schedule.getDepartureTime().equals(dto.getDepartureTime())) {
                schedule.setDepartureTime(dto.getDepartureTime());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getArrivalTime() != null) {
            if (!schedule.getArrivalTime().equals(dto.getArrivalTime())) {
                schedule.setArrivalTime(dto.getArrivalTime());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getNote() != null) {
            if (!schedule.getNote().equals(dto.getNote())) {
                schedule.setNote(dto.getNote());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getType() != null) {
            if (!schedule.getType().equals(dto.getType().getValue())) {
                schedule.setType(dto.getType().getValue());
                isUpdated = true;
            }
            isValidField = true;
        }

        if (isUpdated) {
            schedule.setUpdatedAt(new Date());
        } else if (isValidField) {
            throw new NotModifiedException("Không có sự thay đổi nào của lịch trình!");
        } else {
            throw new InvalidFieldException("Trường cần cập nhật không tồn tại trong lịch trình!");
        }
    }
}
