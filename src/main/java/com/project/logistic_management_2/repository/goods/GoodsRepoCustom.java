package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;

import java.util.List;

public interface GoodsRepoCustom {
    List<GoodsDTO> getGoodsByFilter(String warehouseId);
}
