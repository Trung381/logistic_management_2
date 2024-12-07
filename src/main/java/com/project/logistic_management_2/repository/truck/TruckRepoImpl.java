package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.QTruck;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TruckRepoImpl extends BaseRepo implements TruckRepoCustom {
    public TruckRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Truck> getTruckById(Integer id) {
        QTruck qTruck = QTruck.truck;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTruck.id.eq(id));
        builder.and(qTruck.deleted.eq(false));// Chỉ lấy bản ghi chưa bị xóa
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
        builder.and(qTruck.deleted.eq(false));
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
        builder.and(qTruck.deleted.eq(false));
        return query.from(qTruck)
                .where(builder)
                .select(qTruck)
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long delete(Integer id) {
        QTruck qTruck = QTruck.truck;
        BooleanBuilder builder = new BooleanBuilder()
                .and(qTruck.id.eq(id))
                .and(qTruck.deleted.eq(false));

        return query.update(qTruck)
                .where(builder)
                .set(qTruck.deleted, true)
                .execute();
    }

}
