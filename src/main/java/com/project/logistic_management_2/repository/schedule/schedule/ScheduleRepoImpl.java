package com.project.logistic_management_2.repository.schedule.schedule;

import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.enums.Pagination;
import com.project.logistic_management_2.enums.schedule.ScheduleStatus;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
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
                schedule.type.as("type"),
                schedule.status.as("status"),
                schedule.createdAt.as("createdAt"),
                schedule.updatedAt.as("updatedAt")
        );
    }

    @Override
    public List<ScheduleDTO> getAll(int page, String driverid, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.deleted.eq(false));

        if (driverid != null && !driverid.isBlank()) {
            builder.and(user.id.eq(driverid));
        }

        if (truckLicense != null && !truckLicense.isBlank()) {
            builder.and(schedule.truckLicense.eq(truckLicense));
        }

        if (fromDate != null && toDate != null) {
            builder.and(schedule.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(schedule.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(schedule.createdAt.loe(toDate));
        }

        long offset = (long) (page - 1) * Pagination.TEN.getSize();

        return query.from(schedule)
                .innerJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(builder)
                .select(scheduleProjection())
                .orderBy(schedule.updatedAt.desc())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public List<ScheduleDTO> getAll(String driverid, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.deleted.eq(false));

        if (driverid != null && !driverid.isBlank()) {
            builder.and(user.id.eq(driverid));
        }

        if (truckLicense != null && !truckLicense.isBlank()) {
            builder.and(schedule.truckLicense.eq(truckLicense));
        }

        if (fromDate != null && toDate != null) {
            builder.and(schedule.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(schedule.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(schedule.createdAt.loe(toDate));
        }

        return query.from(schedule)
                .innerJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(builder)
                .select(scheduleProjection())
                .orderBy(schedule.updatedAt.desc())
                .fetch();
    }

    @Override
    public List<ScheduleDTO> getByFilter(String license, YearMonth period) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.deleted.eq(false));

        if (license != null && !license.isBlank()) {
            builder.and(schedule.truckLicense.eq(license).or(schedule.moocLicense.eq(license)));
        }
        //Tìm theo chu kỳ nếu period hợp lệ
        if (period != null) {
            Date startDate = Date.valueOf(period.atDay(1).atStartOfDay().toLocalDate());
            Date endDate = Date.valueOf(period.plusMonths(1).atDay(1).atStartOfDay().toLocalDate());
            builder.and(schedule.createdAt.between(startDate, endDate));
        }

        return query.from(schedule)
                .innerJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .where(builder)
                .select(scheduleProjection())
                .orderBy(schedule.updatedAt.desc())
                .fetch();
    }

    @Override
    public Optional<ScheduleDTO> getByID(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false));

        return Optional.ofNullable(
                query.from(schedule)
                        .leftJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                        .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
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

    @Override
    @Modifying
    @Transactional
    public long markComplete(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false));
        return query.update(schedule)
                .where(builder)
                .set(schedule.status, 2) //đã hoàn thành
                .set(schedule.arrivalTime, new java.util.Date())
                .execute();
    }

    @Override
    public List<ScheduleSalaryDTO> exportScheduleSalary(String driverId, YearMonth period) {
        Date startDate = Date.valueOf(LocalDate.now().atStartOfDay().toLocalDate());
        Date endDate = Date.valueOf(LocalDate.now().plusMonths(1).atStartOfDay().toLocalDate());

        if (period != null) {
            startDate = Date.valueOf(period.atDay(1).atStartOfDay().toLocalDate());
            endDate = Date.valueOf(period.plusMonths(1).atDay(1).atStartOfDay().toLocalDate());
        }

        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.createdAt.between(startDate, endDate))
                .and(user.id.eq(driverId));

        ConstructorExpression<ScheduleSalaryDTO> expression = Projections.constructor(ScheduleSalaryDTO.class,
                user.fullName.as("driverName"),
                scheduleConfig.placeA.as("placeA"),
                scheduleConfig.placeB.as("placeB"),
                scheduleConfig.amount.min().coalesce(0f).as("amount"),
                schedule.id.count().castToNum(Integer.class).coalesce(0).as("count"),
                scheduleConfig.amount.min().multiply(schedule.id.count()).castToNum(Float.class).coalesce(0f).as("total")
        );

        return query.from(user)
                .innerJoin(truck).on(user.id.eq(truck.driverId))
                .innerJoin(schedule).on(truck.licensePlate.eq(schedule.truckLicense))
                .innerJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .where(builder)
                .groupBy(scheduleConfig.placeA, scheduleConfig.placeB)
                .select(expression)
                .fetch();
    }

    @Override
    public List<ScheduleDTO> findByLicensePlate(String licensePlate) {
        // Xây dựng điều kiện tìm kiếm
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.deleted.eq(false)); // Chỉ tìm lịch trình không bị xóa

        // Thêm điều kiện tìm theo truckLicense hoặc moocLicense
        if (licensePlate != null && !licensePlate.isBlank()) {
            builder.and(schedule.truckLicense.eq(licensePlate)
                    .or(schedule.moocLicense.eq(licensePlate)));
        }

        return query.from(schedule)
                .leftJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .leftJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .leftJoin(user).on(truck.driverId.eq(user.id))
                .where(builder) // Áp dụng điều kiện tìm kiếm
                .select(scheduleProjection()) // Chọn các trường cần thiết
                .orderBy(schedule.updatedAt.desc()) // Sắp xếp theo thời gian cập nhật mới nhất
                .fetch(); // Lấy dữ liệu
    }



    @Override
    public List<ScheduleDTO> exportReport(String license, YearMonth period) {
        ConstructorExpression<ScheduleDTO> expression = Projections.constructor(ScheduleDTO.class,
                schedule.scheduleConfigId.coalesce("Chạy nội bộ").as("scheduleConfigId"),
                scheduleConfig.placeA.as("placeA"),
                scheduleConfig.placeB.as("placeB"),
                JPAExpressions.select(truck.driverId.as("driverId"))
                        .from(truck)
                        .where(schedule.truckLicense.eq(truck.licensePlate)),
                JPAExpressions.select(user.fullName)
                        .from(user, truck)
                        .where(schedule.truckLicense.eq(truck.licensePlate)
                                .and(truck.driverId.eq(user.id))
                        ),
                schedule.truckLicense.as("truckLicense"),
                schedule.moocLicense.as("moocLicense"),
                schedule.departureTime.as("departureTime"),
                schedule.arrivalTime.as("arrivalTime"),
                schedule.id.count().castToNum(Integer.class).coalesce(0).as("count")
        );

        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.truckLicense.eq(license));

        if (period != null) {
            Date startDate = Date.valueOf(period.atDay(1).atStartOfDay().toLocalDate());
            Date endDate = Date.valueOf(period.plusMonths(1).atDay(1).atStartOfDay().toLocalDate());
            builder.and(schedule.createdAt.between(startDate, endDate));
        }

        return query.from(schedule)
                .leftJoin(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .where(builder)
                .groupBy(schedule.truckLicense, schedule.moocLicense, schedule.departureTime, schedule.arrivalTime, schedule.scheduleConfigId)
                .select(expression)
                .fetch();
    }

    @Override
    public long countByID(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false));

        Long res = query.from(schedule)
                .where(builder)
                .select(schedule.id.count().coalesce(0L))
                .fetchOne();

        return res != null ? res : 0;
    }

    @Override
    public ScheduleStatus getStatusByID(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(schedule.id.eq(id))
                .and(schedule.deleted.eq(false));

        Integer statusNumber = query.from(schedule)
                .where(builder)
                .select(schedule.status)
                .fetchOne();
        return statusNumber != null ? ScheduleStatus.valueOf(statusNumber) : null;
    }
}
