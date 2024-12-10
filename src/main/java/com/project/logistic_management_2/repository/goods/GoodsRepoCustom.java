package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.entity.Goods;

import java.util.List;

public interface GoodsRepoCustom {
    List<Goods> getGoodsByWareHouseId(String wareHouseId);
}
