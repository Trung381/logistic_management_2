package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;
import com.project.logistic_management_2.enums.IDKey;
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

        return dtos.stream().map(dto ->
                ScheduleConfig.builder()
                        .id(Utils.genID(IDKey.SCHEDULE_CONFIG))
                        .placeA(dto.getPlaceA())
                        .placeB(dto.getPlaceB())
                        .amount(dto.getAmount())
                        .note(dto.getNote())
                        .deleted(false)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build()
        ).collect(Collectors.toList());
    }

    public void updateScheduleConfig(ScheduleConfig config, ScheduleConfigDTO dto) {
        if (dto == null) return;
        config.setPlaceA(dto.getPlaceA());
        config.setPlaceB(dto.getPlaceB());
        config.setAmount(dto.getAmount());
        config.setNote(dto.getNote());
        config.setUpdatedAt(new Date());
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
