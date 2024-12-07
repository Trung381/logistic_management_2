package com.project.logistic_management_2.service.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.truck.TruckMapper;
import com.project.logistic_management_2.repository.truck.TruckRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl extends BaseService  implements TruckService {

    private final TruckRepo repository;
    private final TruckMapper mapper;

    @Override
    public TruckDTO createTruck(TruckDTO truckDTO) {
        Truck truck = mapper.toTruck(truckDTO);
        repository.save(truck);
        return mapper.toTruckDTO(truck);
    }

    @Override
    public List<TruckDTO> getAllTrucks() {
        List<Truck> trucks = repository.getAllTrucks();
        return trucks.stream().map(mapper::toTruckDTO).toList();
    }


    @Override
    public TruckDTO getTruckById(Integer id) {
        if(id == null) return null;
        Truck truck = repository.getTruckById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe cần tìm!"));
        return mapper.toTruckDTO(truck);
    }

    public TruckDTO getTruckByLicensePlate(String licensePlate) {
        Truck truck = repository.getTruckByLicensePlate(licensePlate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe với biển số: " + licensePlate));
        return mapper.toTruckDTO(truck);
    }

    @Override
    public TruckDTO updateTruck(Integer id, TruckDTO truckDTO) {
        Truck truck = repository.getTruckById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe cần tìm!"));

        mapper.updateTruck(truck, truckDTO);
        repository.save(truck);
        return mapper.toTruckDTO(truck);
    }

    @Override
    public long deleteTruck(Integer id) {
       Truck truck = repository.getTruckById(id)
               .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe"));
       return repository.delete(id);
    }
}

