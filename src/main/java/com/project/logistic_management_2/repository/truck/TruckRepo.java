package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckRepo extends JpaRepository<Truck,  String>, TruckRepoCustom{
}
