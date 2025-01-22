package com.project.logistic_management_2.repository.warehouses;

import com.project.logistic_management_2.dto.warehouse.WarehousesDTO;
import com.project.logistic_management_2.entity.warehouse.Warehouses;

import java.util.List;
import java.util.Optional;

public interface WarehousesRepoCustom {
    List<WarehousesDTO> getAll();
}
