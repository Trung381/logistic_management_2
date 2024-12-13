package com.project.logistic_management_2.service.warehouses;

import com.project.logistic_management_2.dto.request.WarehousesDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WarehousesService {
    List<WarehousesDTO> getAllWarehouses();
}
