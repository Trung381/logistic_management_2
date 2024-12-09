package com.project.logistic_management_2.repository.truck;

import com.project.logistic_management_2.entity.QTruck;
import com.project.logistic_management_2.entity.Truck;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
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

    @Override
    @Modifying
    @Transactional
    public long updateStatus(@NotNull String license, int status) {
        if (status < -1 || status > 1) {
            return 0;
        }
        QTruck qTruck = QTruck.truck;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qTruck.licensePlate.eq(license))
                .and(qTruck.deleted.eq(false));

        return query.update(qTruck)
                .where(builder)
                .set(qTruck.status, status)
                .execute();
    }

    public List<Truck> getTrucksByType(Integer type) {
        // Tạo một đối tượng QTruck (QueryDSL) để xây dựng các truy vấn
        QTruck qTruck = QTruck.truck;

        // Tạo một BooleanBuilder để xây dựng các điều kiện lọc trong truy vấn
        BooleanBuilder builder = new BooleanBuilder();

        // Thêm điều kiện lọc: cột "type" phải bằng giá trị "type" được truyền vào
        builder.and(qTruck.type.eq(type)); // Lọc theo giá trị của type (ví dụ: 0 hoặc 1)

        // Thêm điều kiện lọc: chỉ lấy các bản ghi chưa bị xóa (cột "deleted" = 1)
        builder.and(qTruck.deleted.eq(1)); // Lọc các bản ghi chưa bị xóa (deleted = 1)

        // Sử dụng QueryDSL để thực hiện truy vấn
        return query.from(qTruck)               // Chọn bảng Truck làm nguồn truy vấn
                .where(builder)            // Áp dụng các điều kiện lọc đã xây dựng
                .select(qTruck)            // Chọn tất cả các cột trong bảng Truck
                .fetch();                  // Thực thi truy vấn và trả về danh sách kết quả
    }
}
