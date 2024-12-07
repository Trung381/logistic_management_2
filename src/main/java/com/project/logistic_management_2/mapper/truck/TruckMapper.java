package com.project.logistic_management_2.mapper.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TruckMapper {
    public Truck toTruck(TruckDTO truckDTO) {
        if(truckDTO==null)  return null;
        return Truck.builder()
                .id(Utils.genID(IDKey.TRUCK))
                .driverId(truckDTO.getDriverId())
                .licensePlate(truckDTO.getLicensePlate())
                .capacity(truckDTO.getCapacity())
                .type(truckDTO.getType())
                .note(truckDTO.getNote())
                .status(1)
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public void updateTruck(Truck truck, TruckDTO truckDTO) {
        if (truckDTO == null) return;
        truck.setLicensePlate(truckDTO.getLicensePlate());
        truck.setCapacity(truckDTO.getCapacity());
        truck.setNote(truckDTO.getNote());
        truck.setType(truckDTO.getType());
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
        truckDTO.setType(truck.getType());
        truckDTO.setNote(truck.getNote());
        truckDTO.setStatus(truck.getStatus());
        truckDTO.setCreatedAt(truck.getCreatedAt());
        truckDTO.setUpdatedAt(truck.getUpdatedAt());
        return truckDTO;
    }
}
