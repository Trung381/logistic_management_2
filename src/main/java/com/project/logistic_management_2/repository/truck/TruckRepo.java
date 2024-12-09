package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckRepo extends JpaRepository<Truck,  String>, TruckRepoCustom{
}
