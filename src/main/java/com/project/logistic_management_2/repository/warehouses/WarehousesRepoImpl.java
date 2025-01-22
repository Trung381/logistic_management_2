package com.project.logistic_management_2.repository.warehouses;

import com.project.logistic_management_2.dto.warehouse.WarehousesDTO;
import com.project.logistic_management_2.entity.warehouse.Warehouses;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.project.logistic_management_2.entity.goods.QGoods.goods;
import static com.project.logistic_management_2.entity.warehouse.QWarehouses.warehouses;

import java.util.List;

@Repository
public class WarehousesRepoImpl extends BaseRepo implements WarehousesRepoCustom {
    public WarehousesRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<WarehousesDTO> warehousesProjection() {
        return Projections.constructor(WarehousesDTO.class,
                warehouses.id.as("id"),
                warehouses.name.as("name"),
                warehouses.note.as("note"),
                warehouses.createdAt.as("created_at"),
                warehouses.updatedAt.as("updated_at")
                );
    }

    @Override
    public List<WarehousesDTO> getAll() {

        return query.from(warehouses)
                .select(warehousesProjection())
                .fetch();
    }
}
