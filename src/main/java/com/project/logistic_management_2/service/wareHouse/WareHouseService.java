package com.project.logistic_management_2.service.wareHouse;

import com.project.logistic_management_2.dto.request.WareHouseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WareHouseService {
    List<WareHouseDTO> getAllWareHouses();
}
