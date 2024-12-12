package com.project.logistic_management_2.repository.warehouses;

import com.project.logistic_management_2.entity.Warehouses;

import java.util.Optional;

public interface WarehousesRepoCustom {
    Optional<Warehouses> getWarehouseById(String id);
}
