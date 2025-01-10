package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.Truck;

import java.util.List;
import java.util.Optional;

public interface TruckRepoCustom {
    Optional<TruckDTO> getTruckById(Integer id);
    Optional<TruckDTO> getTruckByLicense(String licensePlate);
    List<TruckDTO> getAllTrucks();
    List<TruckDTO> getTrucksByType(Integer type);
    long delete(Integer id);
}
