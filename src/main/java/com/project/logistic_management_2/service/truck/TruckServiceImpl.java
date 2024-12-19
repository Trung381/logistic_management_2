package com.project.logistic_management_2.service.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.entity.Schedule;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
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
    private final PermissionType type = PermissionType.TRUCKS;

    @Override
    public TruckDTO createTruck(TruckDTO truckDTO) {
        checkPermission(type, PermissionKey.WRITE); // check quyền tạo
        Truck truck = mapper.toTruck(truckDTO);
        repository.save(truck);
        return repository.getTruckById(truck.getId()).get();
    }

    @Override
    public List<TruckDTO> getAllTrucks() {
        checkPermission(type, PermissionKey.VIEW); // Kiểm tra quyền xem
        return repository.getAllTrucks();
    }

    @Override
    public List<TruckDTO> getTrucksByType(Integer Type) {
//        checkPermission(type, PermissionKey.VIEW);
        List<TruckDTO> trucks = repository.getTrucksByType(Type);
        if (trucks.isEmpty()) {
            throw new NotFoundException("Không tìm thấy xe với loại: " + Type);
        }
        return trucks;
    }

    @Override
    public TruckDTO getTruckByLicensePlate(String licensePlate) {
        checkPermission(type, PermissionKey.VIEW);
        return repository.getTruckByLicensePlate(licensePlate)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy xe với biển số: " + licensePlate));
    }

    @Override
    public TruckDTO updateTruck(Integer id, TruckDTO dto) {
        checkPermission(type, PermissionKey.WRITE);
        TruckDTO truckDTO = repository.getTruckById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe cần tìm!"));

        Truck truck = mapper.toTruck(truckDTO);
        mapper.updateTruck(truck, dto);
        repository.save(truck);
        return repository.getTruckById(id).get();
    }

    @Override
    public long deleteTruck(Integer id) {
        checkPermission(type, PermissionKey.DELETE); // Kiểm tra quyền xóa
        TruckDTO truck = repository.getTruckById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe"));
        return repository.delete(id);
    }

}

