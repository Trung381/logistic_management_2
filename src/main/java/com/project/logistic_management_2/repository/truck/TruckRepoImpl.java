package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
                truck.status.as("status"),
                truck.note.as("note"),
                truck.createdAt.as("createdAt"),
                truck.updatedAt.as("updatedAt")
        );
    }

    @Override
    public Optional<TruckDTO> getTruckById(Integer id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truck.id.eq(id));
        builder.and(truck.deleted.eq(false));// Chỉ lấy bản ghi chưa bị xóa

        return Optional.ofNullable(
                query.from(truck)
                        .innerJoin(user).on(truck.driverId.eq(user.id))
                        .where(builder)
                        .select(expression())
                        .fetchOne()
        );
    }

    @Override
    public Optional<TruckDTO> getTruckByLicensePlate(String licensePlate) {
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

    @Override
    @Modifying
    @Transactional
    public long updateStatus(@NotNull String license, int status) {
        if (status < -1 || status > 1) {
            return 0;
        }
        BooleanBuilder builder = new BooleanBuilder()
                .and(truck.licensePlate.eq(license))
                .and(truck.deleted.eq(false));

        return query.update(truck)
                .where(builder)
                .set(truck.status, status)
                .execute();
    }

    public List<TruckDTO> getTrucksByType(Integer type) {
        // Tạo một BooleanBuilder để xây dựng các điều kiện lọc trong truy vấn
        BooleanBuilder builder = new BooleanBuilder();

        // Thêm điều kiện lọc: cột "type" phải bằng giá trị "type" được truyền vào
        builder.and(truck.type.eq(type)); // Lọc theo giá trị của type (ví dụ: 0 hoặc 1)

        // Thêm điều kiện lọc: chỉ lấy các bản ghi chưa bị xóa (cột "deleted" = 1)
        builder.and(truck.deleted.eq(false)); // Lọc các bản ghi chưa bị xóa (deleted = false)

        // Sử dụng QueryDSL để thực hiện truy vấn
        return query.from(truck)
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(builder)
                .select(expression())
                .fetch();
    }
}
