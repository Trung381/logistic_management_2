package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.QTruck;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Optional;

public class TruckRepoImpl extends BaseRepo implements TruckRepoCustom {
    public TruckRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Truck> getTruckById(String id) {
        QTruck qTruck = QTruck.truck;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTruck.id.eq(id));
        builder.and(qTruck.deleted.eq(1));// Chỉ lấy bản ghi chưa bị xóa
        Truck truck = query.from(qTruck)
                .where(builder)
                .select(qTruck)
                .fetchOne();
        return Optional.ofNullable(truck);
    }

    @Override
    public Optional<Truck> getTruckByLicensePlate(String licensePlate) {
        QTruck qTruck = QTruck.truck;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTruck.licensePlate.eq(licensePlate));
        builder.and(qTruck.deleted.eq(1));
        Truck truck = query.from(qTruck)
                .where(builder)
                .select(qTruck)
                .fetchOne();
        return Optional.ofNullable(truck);
    }

    @Override
    public List<Truck> getAllTrucks() {
        QTruck qTruck = QTruck.truck;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTruck.deleted.eq(1));
        return query.from(qTruck)
                .where(builder)
                .select(qTruck)
                .fetch();
    }

    @Override
    public List<Truck> getTrucksByType(Integer type) {
        QTruck qTruck = QTruck.truck;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTruck.type.eq(type)); // Lọc theo type (0 hoặc 1)
        builder.and(qTruck.deleted.eq(1)); // Lọc các bản ghi chưa bị xóa

        return query.from(qTruck)
                .where(builder)
                .select(qTruck)
                .fetch();
    }
}
