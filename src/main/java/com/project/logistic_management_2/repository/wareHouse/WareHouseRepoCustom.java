package com.project.logistic_management_2.repository.wareHouse;

import com.project.logistic_management_2.entity.WareHouse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface WareHouseRepoCustom {
    Optional<WareHouse> getWareHouseById(String id);
}
