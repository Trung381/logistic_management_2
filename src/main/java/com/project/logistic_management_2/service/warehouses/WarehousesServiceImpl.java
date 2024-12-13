package com.project.logistic_management_2.service.warehouses;

import com.project.logistic_management_2.dto.request.WarehousesDTO;
import com.project.logistic_management_2.entity.Warehouses;
import com.project.logistic_management_2.mapper.warehouses.WarehousesMapper;
import com.project.logistic_management_2.repository.warehouses.WarehousesRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehousesServiceImpl extends BaseService implements WarehousesService {

    private final WarehousesRepo repository;
    private final WarehousesMapper mapper;

    @Override
    public List<WarehousesDTO> getAllWarehouses() {
        List<Warehouses> warehousesList = repository.findAll();
        return mapper.toWarehouseDTOList(warehousesList);
    }
}