package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.enums.truck.TruckStatus;
import com.project.logistic_management_2.enums.truck.TruckType;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.entity.QTransaction.transaction;
import static com.project.logistic_management_2.entity.QTruck.truck;
import static com.project.logistic_management_2.entity.QUser.user;

@Repository
public class TruckRepoImpl extends BaseRepo implements TruckRepoCustom {
    public TruckRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<TruckDTO> expression() {
        return Projections.constructor(TruckDTO.class,
                truck.id.as("id"),
                truck.licensePlate.as("licensePlate"),
                truck.capacity.as("capacity"),
                truck.driverId.as("driverId"),
                user.fullName.as("driverName"),
                truck.type.as("type"),
                new CaseBuilder()
                        .when(truck.type.eq(TruckType.TRUCK_HEAD.getValue())).then(TruckType.TRUCK_HEAD.getDescription())
                        .when(truck.type.eq(TruckType.MOOC.getValue())).then(TruckType.MOOC.getDescription())
                        .otherwise("Không xác định")
                        .as("typeDescription"),
                truck.status.as("status"),
                new CaseBuilder()
                        .when(truck.status.eq(TruckStatus.APPROVED_SCHEDULE.getValue())).then(TruckStatus.APPROVED_SCHEDULE.getDescription())
                        .when(truck.status.eq(TruckStatus.AVAILABLE.getValue())).then(TruckStatus.AVAILABLE.getDescription())
                        .when(truck.status.eq(TruckStatus.MAINTENANCE.getValue())).then(TruckStatus.MAINTENANCE.getDescription())
                        .when(truck.status.eq(TruckStatus.PENDING_SCHEDULE.getValue())).then(TruckStatus.PENDING_SCHEDULE.getDescription())
                        .otherwise("Không xác định")
                        .as("statusDescription"),
                truck.note.as("note"),
                truck.createdAt.as("createdAt"),
                truck.updatedAt.as("updatedAt")
        );
    }

    @Override
    public Optional<TruckDTO> getTruckById(Integer id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truck.id.eq(id));
        builder.and(truck.deleted.eq(false));

        return Optional.ofNullable(
                query.from(truck)
                        .innerJoin(user).on(truck.driverId.eq(user.id))
                        .where(builder)
                        .select(expression())
                        .fetchOne()
        );
    }

    @Override
    public Optional<TruckDTO> getTruckByLicense(String licensePlate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truck.licensePlate.eq(licensePlate));
        builder.and(truck.deleted.eq(false));

        return Optional.ofNullable(
                query.from(truck)
                        .innerJoin(user).on(truck.driverId.eq(user.id))
                        .where(builder)
                        .select(expression())
                        .fetchOne()
        );
    }

    @Override
    public List<TruckDTO> getAllTrucks() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truck.deleted.eq(false));

        return query.from(truck)
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(builder)
                .select(expression())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long delete(Integer id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(truck.id.eq(id))
                .and(truck.deleted.eq(false));

        return query.update(truck)
                .where(builder)
                .set(truck.deleted, true)
                .execute();
    }

    public List<TruckDTO> getTrucksByType(Integer type) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(truck.type.eq(type));

        builder.and(truck.deleted.eq(false));

        return query.from(truck)
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(builder)
                .select(expression())
                .fetch();
    }
}
