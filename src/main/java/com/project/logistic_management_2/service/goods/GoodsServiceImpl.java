package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends BaseService implements GoodsService {

    private final GoodsRepo repository;

    @Override
    public List<GoodsDTO> getGoodsByFilter(String warehouseId) {
        return repository.getGoodsByFilter(warehouseId);
    }
}
