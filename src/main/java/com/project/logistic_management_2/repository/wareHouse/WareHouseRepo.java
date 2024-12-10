package com.project.logistic_management_2.repository.wareHouse;

import com.project.logistic_management_2.entity.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepo extends JpaRepository<WareHouse, String>, WareHouseRepoCustom {

}
