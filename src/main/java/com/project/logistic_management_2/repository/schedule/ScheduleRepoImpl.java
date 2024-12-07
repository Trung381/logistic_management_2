package com.project.logistic_management_2.repository.schedule;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.project.logistic_management_2.entity.QSchedule.schedule;
import static com.project.logistic_management_2.entity.QScheduleConfig.scheduleConfig;
import static com.project.logistic_management_2.entity.QUser.user;
import static com.project.logistic_management_2.entity.QTruck.truck;

@Repository
public class ScheduleRepoImpl extends BaseRepo implements ScheduleRepoCustom {
    public ScheduleRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<ScheduleDTO> scheduleProjection() {
        return Projections.constructor(ScheduleDTO.class,
                schedule.id.as("id"),
                schedule.scheduleConfigId.as("scheduleConfigId"),
                //Địa điểm A
                scheduleConfig.placeA.as("placeA"),
                //Địa điểm B
                scheduleConfig.placeB.as("placeB"),
                //Gia tien
                scheduleConfig.amount.as("amount"),
                // Thong tin tai xe
                truck.driverId.as("driverId"),
                JPAExpressions.select(user.fullName.as("driverName"))
                        .from(user)
                        .where(truck.driverId.eq(user.id)),
                //Bien so xe tai
                schedule.truckLicense.as("truckLicense"),
                schedule.moocLicense.as("moocLicense"),
                //Thoi gian giao nhan hang
                schedule.departureTime.as("departureTime"),
                schedule.arrivalTime.as("arrivalTime"),

                schedule.note.as("note"),
                schedule.attachDocument.as("attachDocument"),
                schedule.status.as("status"),
                schedule.createdAt.as("createdAt"),
                schedule.updatedAt.as("updatedAt")
        );
    }

    @Override
    public List<ScheduleDTO> getAll() {
        return query.from(schedule)
                .innerJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .where(schedule.deleted.eq(false))
                .select(scheduleProjection())
                .fetch();
    }

    @Override
    public Optional<ScheduleDTO> getByID(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false));

        return Optional.ofNullable(
                query.from(schedule)
                        .innerJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                        .where(builder)
                        .select(scheduleProjection())
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false));

        return query.update(schedule)
                .where(builder)
                .set(schedule.deleted, true)
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public long approve(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false))
                .and(schedule.status.eq(0));

        return query.update(schedule)
                .where(builder)
                .set(schedule.status, 1)
                .execute();
    }
}
