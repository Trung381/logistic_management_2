package com.project.logistic_management_2.repository.wareHouse;

import com.project.logistic_management_2.entity.QTransaction;
import com.project.logistic_management_2.entity.QWareHouse;
import com.project.logistic_management_2.entity.Transaction;
import com.project.logistic_management_2.entity.WareHouse;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WareHouseRepoImpl extends BaseRepo implements WareHouseRepoCustom {
    public WareHouseRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }
    @Override
    public Optional<WareHouse> getWareHouseById(String id) {
        QWareHouse qWareHouse = QWareHouse.wareHouse;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qWareHouse.id.eq(id));
        WareHouse wareHouse = query.from(qWareHouse)
                .where(builder)
                .select(qWareHouse)
                .fetchOne();
        return Optional.ofNullable(wareHouse);
    }
}
