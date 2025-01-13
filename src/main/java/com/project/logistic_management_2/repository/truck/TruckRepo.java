package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.Truck;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TruckRepo extends JpaRepository<Truck,  String>, TruckRepoCustom{
    Optional<Truck> findByLicensePlate(String license);
    boolean existsByLicensePlate(@Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự") String licensePlate);
}
