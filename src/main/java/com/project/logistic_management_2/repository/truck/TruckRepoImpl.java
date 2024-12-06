package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.QTruck;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;

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

}
