package com.project.logistic_management_2.repository.warehouses;

import com.project.logistic_management_2.entity.warehouse.Warehouses;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.project.logistic_management_2.entity.warehouse.QWarehouses.warehouses;

import java.util.Optional;

@Repository
public class WarehousesRepoImpl extends BaseRepo implements WarehousesRepoCustom {
    public WarehousesRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }
    @Override
    public Optional<Warehouses> getWarehouseById(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(warehouses.id.eq(id));
        return Optional.ofNullable(
                query.from(warehouses)
                        .where(builder)
                        .select(warehouses)
                        .fetchOne()
        );
    }
}
