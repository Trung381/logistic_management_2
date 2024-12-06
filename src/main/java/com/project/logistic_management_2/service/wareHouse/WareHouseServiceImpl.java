package com.project.logistic_management_2.service.wareHouse;

import com.project.logistic_management_2.dto.request.WareHouseDTO;
import com.project.logistic_management_2.entity.WareHouse;
import com.project.logistic_management_2.mapper.wareHouse.WareHouseMapper;
import com.project.logistic_management_2.repository.wareHouse.WareHouseRepo;
import com.project.logistic_management_2.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WareHouseServiceImpl extends BaseService<WareHouseRepo, WareHouseMapper> implements WareHouseService {

    public WareHouseServiceImpl(WareHouseRepo repo, WareHouseMapper mapper) {
        super(repo,mapper);
    }

    @Override
    public List<WareHouseDTO> getAllWareHouses() {
        List<WareHouse> wareHouseList = repository.findAll();
        return mapper.toWareHouseDTOList(wareHouseList);
    }
}
