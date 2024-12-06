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
    public Goods toGoods(GoodsDTO dto) {
        if (dto == null) return null;

        return Goods.builder()
                .warehouseId(dto.getWarehouseId())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .amount(dto.getAmount())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

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

    public void updateGoods(Goods goods, GoodsDTO dto) {
        if (dto == null || goods == null) return;

        goods.setName(dto.getName());
        goods.setQuantity(dto.getQuantity());
        goods.setAmount(dto.getAmount());
        goods.setUpdatedAt(new Date());
    }
}
