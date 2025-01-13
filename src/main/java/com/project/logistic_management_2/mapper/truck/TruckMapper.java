package com.project.logistic_management_2.mapper.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.enums.truck.TruckStatus;
import com.project.logistic_management_2.enums.truck.TruckType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TruckMapper {
    public Truck toTruck(TruckDTO truckDTO) {
        if(truckDTO==null)  return null;
        return Truck.builder()
                .id(truckDTO.getId() != null ? truckDTO.getId() : null)
                .driverId(truckDTO.getDriverId())
                .licensePlate(truckDTO.getLicensePlate())
                .capacity(truckDTO.getCapacity())
                .type(truckDTO.getType().getValue())
                .note(truckDTO.getNote())
                .status(TruckStatus.AVAILABLE.getValue())
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }
    public List<Truck> toTruckList(List<TruckDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(truckDTO ->
                Truck.builder()
                        .id(truckDTO.getId() != null ? truckDTO.getId() : null)
                        .driverId(truckDTO.getDriverId())
                        .licensePlate(truckDTO.getLicensePlate())
                        .capacity(truckDTO.getCapacity())
                        .type(truckDTO.getType().getValue())
                        .note(truckDTO.getNote())
                        .status(TruckStatus.AVAILABLE.getValue())
                        .deleted(false)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build()
        ).collect(Collectors.toList());
    }

    public void updateTruck(Truck truck, TruckDTO truckDTO) {
        if (truckDTO == null) return;
        truck.setLicensePlate(truckDTO.getLicensePlate());
        truck.setCapacity(truckDTO.getCapacity());
        truck.setNote(truckDTO.getNote());
        truck.setType(truckDTO.getType().getValue());
        truck.setStatus(truck.getStatus());
        truck.setUpdatedAt(new Date());
    }

    public TruckDTO toTruckDTO(Truck truck) {
        if (truck == null) return null;
        TruckDTO truckDTO = new TruckDTO();
        truckDTO.setId(truck.getId());
        truckDTO.setDriverId(truck.getDriverId());
        truckDTO.setLicensePlate(truck.getLicensePlate());
        truckDTO.setCapacity(truck.getCapacity());
        truckDTO.setType(TruckType.valueOf(truck.getType()));
        truckDTO.setNote(truck.getNote());
        truckDTO.setStatus(TruckStatus.valueOf(truck.getStatus()));
        truckDTO.setCreatedAt(truck.getCreatedAt());
        truckDTO.setUpdatedAt(truck.getUpdatedAt());
        return truckDTO;
    }
}
