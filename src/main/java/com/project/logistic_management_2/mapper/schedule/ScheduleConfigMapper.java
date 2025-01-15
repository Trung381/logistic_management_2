package com.project.logistic_management_2.mapper.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.entity.schedule.ScheduleConfig;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collections;
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

        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field srcField : fields) {
            srcField.setAccessible(true);
            try {
                Object newValue = srcField.get(dto);
                if (newValue != null) {
                    Field targetField = config.getClass().getField(srcField.getName());
                    targetField.setAccessible(true);
                    if (!newValue.equals(targetField.get(config))) {
                        targetField.set(config, newValue);
                    }
                    targetField.setAccessible(false);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                //ignore
            } finally {
                srcField.setAccessible(false);
            }
        }
    }
}
