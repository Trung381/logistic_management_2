package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.Truck;

import java.util.List;
import java.util.Optional;

public interface TruckRepoCustom {
    Optional<Truck> getTruckById(Integer id);
    Optional<Truck> getTruckByLicensePlate(String licensePlate);
    List<Truck> getAllTrucks();
    List<Truck> getTrucksByType(Integer type);
    long delete(Integer id);
}
