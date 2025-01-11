package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.dto.goods.GoodsDTO;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.logistic_management_2.entity.QGoods.goods;
import static com.project.logistic_management_2.entity.QWarehouses.warehouses;

@Repository
public class GoodsRepoImpl extends BaseRepo implements GoodsRepoCustom {
    public GoodsRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<GoodsDTO> goodsProjection() {
        return Projections.constructor(GoodsDTO.class,
                goods.id.as("id"),
                goods.warehouseId.as("warehouseId"),
                JPAExpressions.select(warehouses.name.as("warehouseName"))
                        .from(warehouses)
                        .where(warehouses.id.eq(goods.warehouseId)),
                goods.name.as("name"),
                goods.quantity.as("quantity"),
                goods.amount.as("amount"),
                goods.createdAt.as("createdAt"),
                goods.updatedAt.as("updatedAt")
                );
    }

    @Override
    public List<GoodsDTO> getGoodsByFilter(String warehouseId) {
        BooleanBuilder builder = new BooleanBuilder();
        if(warehouseId != null) {
            builder.and(goods.warehouseId.eq(warehouseId));
        }

        return query.from(goods)
                .where(builder)
                .select(goodsProjection())
                .fetch();
    }

}
