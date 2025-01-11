package com.project.logistic_management_2.mapper.warehouses;

import com.project.logistic_management_2.dto.request.WarehousesDTO;
import com.project.logistic_management_2.entity.Warehouses;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarehousesMapper {
    public List<WarehousesDTO> toWarehouseDTOList(List<Warehouses> warehousesList) {
        if(warehousesList == null || warehousesList.isEmpty()) return null;
        return warehousesList.stream().map(warehouse ->
                WarehousesDTO.builder()
                        .id(warehouse.getId())
                        .name(warehouse.getName())
                        .note(warehouse.getNote())
                        .createdAt(warehouse.getCreatedAt())
                        .updatedAt(warehouse.getUpdatedAt())
                        .build()
        ).collect(Collectors.toList());
    }
}
