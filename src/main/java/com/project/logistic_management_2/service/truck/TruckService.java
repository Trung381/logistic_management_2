package com.project.logistic_management_2.service.truck;

import com.project.logistic_management_2.dto.request.TruckDTO;
import com.project.logistic_management_2.entity.Truck;

import java.util.List;

public interface TruckService {
    TruckDTO createTruck(TruckDTO truckDTO);
    List<TruckDTO> getAllTrucks();
   // TruckDTO getTruckById(String id);
   List<TruckDTO> getTrucksByType(Integer type);
    TruckDTO getTruckByLicensePlate(String licensePlate);
    TruckDTO updateTruck(String id,TruckDTO truckDTO);
    void deleteTruck(String id);

}
