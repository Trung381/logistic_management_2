package com.project.logistic_management_2.mapper.wareHouse;

import com.project.logistic_management_2.dto.request.WareHouseDTO;
import com.project.logistic_management_2.entity.WareHouse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WareHouseMapper {
    public List<WareHouseDTO> toWareHouseDTOList(List<WareHouse> wareHouseList) {
        if(wareHouseList == null || wareHouseList.isEmpty()) return null;

        return wareHouseList.stream().map(wareHouse ->
                WareHouseDTO.builder()
                        .id(wareHouse.getId())
                        .name(wareHouse.getName())
                        .note(wareHouse.getNote())
                        .createdAt(wareHouse.getCreatedAt())
                        .updatedAt(wareHouse.getUpdatedAt())
                        .build()
        ).collect(Collectors.toList());
    }

}
