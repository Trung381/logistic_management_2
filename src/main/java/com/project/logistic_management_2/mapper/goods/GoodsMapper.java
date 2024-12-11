package com.project.logistic_management_2.mapper.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.entity.Goods;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoodsMapper {

    public List<GoodsDTO> toGoodsDTOList(List<Goods> goodsList) {
        if (goodsList == null || goodsList.isEmpty()) return null;

        return goodsList.stream().map(goods ->
                GoodsDTO.builder()
                        .id(goods.getId())
                        .warehouseId(goods.getWarehouseId())
                        .name(goods.getName())
                        .quantity(goods.getQuantity())
                        .amount(goods.getAmount())
                        .createdAt(goods.getCreatedAt())
                        .updatedAt(goods.getUpdatedAt())
                        .build()
        ).collect(Collectors.toList());
    }

}
