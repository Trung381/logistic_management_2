package com.project.logistic_management_2.service.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.Truck;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TruckService {
    TruckDTO createTruck(TruckDTO truckDTO);
    List<TruckDTO> getAllTrucks();
   List<TruckDTO> getTrucksByType(Integer type);
    TruckDTO getTruckByLicensePlate(String licensePlate);
    TruckDTO updateTruck(Integer id,TruckDTO truckDTO);
    long deleteTruck(Integer id);
    List<Truck> importTruckData(MultipartFile importFile);

}
