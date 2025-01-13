package com.project.logistic_management_2.repository.warehouses;

import com.project.logistic_management_2.entity.warehouse.Warehouses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehousesRepo extends JpaRepository<Warehouses, String>, WarehousesRepoCustom {

}
