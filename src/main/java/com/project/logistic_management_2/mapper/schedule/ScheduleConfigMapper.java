package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.schedule.ScheduleConfig;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.exception.define.InvalidFieldException;
import com.project.logistic_management_2.exception.define.NotModifiedException;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleConfigMapper {
    public ScheduleConfig toScheduleConfig(ScheduleConfigDTO dto) {
        if (dto == null) return null;
        return ScheduleConfig.builder()
                .id(Utils.genID(IDKey.SCHEDULE_CONFIG))
                .placeA(dto.getPlaceA())
                .placeB(dto.getPlaceB())
                .amount(dto.getAmount())
                .note(dto.getNote())
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public List<ScheduleConfig> toScheduleConfigList(List<ScheduleConfigDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toScheduleConfig).collect(Collectors.toList());
    }

    public void updateScheduleConfig(ScheduleConfig config, ScheduleConfigDTO dto) {
        if (dto == null) return;
        boolean isUpdated = false, isValidField = false;

        if (dto.getPlaceA() != null) {
            if (!config.getPlaceA().equals(dto.getPlaceA())) {
                config.setPlaceA(dto.getPlaceA());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getPlaceB() != null) {
            if (!config.getPlaceB().equals(dto.getPlaceB())) {
                config.setPlaceB(dto.getPlaceB());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getAmount() != null) {
            if (!config.getAmount().equals(dto.getAmount())) {
                config.setAmount(dto.getAmount());
                isUpdated = true;
            }
            isValidField = true;
        }
        if (dto.getNote() != null) {
            if (!config.getNote().equals(dto.getNote())) {
                config.setNote(dto.getNote());
                isUpdated = true;
            }
            isValidField = true;
        }

        if (isUpdated) {
            config.setUpdatedAt(new Date());
        } else if (isValidField) {
            throw new NotModifiedException("Không có sự thay đổi nào của cấu hình lịch trình!");
        } else {
            throw new InvalidFieldException("Trường cần cập nhật không tồn tại trong cấu hình lịch trình!");
        }
    }

    public ScheduleConfigDTO toScheduleConfigDTO(ScheduleConfigDTO dto, ScheduleConfig config) {
        if (config == null) return null;
        if (dto == null) {
            return ScheduleConfigDTO.builder()
                    .id(config.getId())
                    .placeA(config.getPlaceA())
                    .placeB(config.getPlaceB())
                    .amount(config.getAmount())
                    .note(config.getNote())
                    .createdAt(config.getCreatedAt())
                    .updatedAt(config.getUpdatedAt())
                    .build();
        }
        dto.setId(config.getId());
        dto.setPlaceA(config.getPlaceA());
        dto.setPlaceB(config.getPlaceB());
        dto.setAmount(config.getAmount());
        dto.setNote(config.getNote());
        dto.setCreatedAt(config.getCreatedAt());
        dto.setUpdatedAt(config.getUpdatedAt());
        return dto;
    }
}
