package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.entity.QGoods;
import com.project.logistic_management_2.repository.BaseRepo;
import com.project.logistic_management_2.entity.Goods;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GoodsRepoImpl extends BaseRepo implements GoodsRepoCustom {
    public GoodsRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Goods> getGoodsByWareHouseId(String wareHouseId) {
        QGoods qGoods = QGoods.goods;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qGoods.warehouseId.eq(wareHouseId));

        return query.from(qGoods)
                .where(builder)
                .select(qGoods)
                .fetch();
    }

}
