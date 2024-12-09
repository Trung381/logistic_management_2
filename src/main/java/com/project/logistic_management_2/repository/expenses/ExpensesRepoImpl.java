package com.project.logistic_management_2.repository.expenses;

import com.project.logistic_management_2.dto.expenses.ExpensesDTO;

import static com.project.logistic_management_2.entity.QExpenses.expenses;
import static com.project.logistic_management_2.entity.QExpensesConfig.expensesConfig;
import static com.project.logistic_management_2.entity.QSchedule.schedule;
import static com.project.logistic_management_2.entity.QTruck.truck;
import static com.project.logistic_management_2.entity.QUser.user;

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
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public class ExpensesRepoImpl extends BaseRepo implements ExpensesRepoCustom {
    public ExpensesRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<ExpensesDTO> expensesProjection() {
        return Projections.constructor(ExpensesDTO.class,
                expenses.id.as("id"),
                JPAExpressions.select(truck.driverId.as("driverId"))
                        .from(truck, schedule)
                        .where(expenses.scheduleId.eq(schedule.id)
                                .and(schedule.truckLicense.eq(truck.licensePlate))
                        ),
                JPAExpressions.select(user.fullName.as("driverName"))
                        .from(user, schedule, truck)
                        .where(expenses.scheduleId.eq(schedule.id)
                                .and(schedule.truckLicense.eq(truck.licensePlate))
                                .and(truck.driverId.eq(user.id))
                        ),
                //Mã lịch trình
                expenses.scheduleId.as("scheduleId"),
                //Thông tin cấu hình chi phí: mã, loại chi phí
                expenses.expensesConfigId.as("expensesConfigId"),
                JPAExpressions.select(expensesConfig.type.as("expensesConfigType"))
                        .from(expensesConfig)
                        .where(expensesConfig.id.eq(expenses.expensesConfigId)),

                expenses.amount.as("amount"),   //Giá tiền
                expenses.note.as("note"),       //Ghi chú
                expenses.imgPath.as("imgPath"), //Đường dẫn ảnh đính kèm
                expenses.status.as("status"),   //Trạng thái chi phí
                expenses.createdAt.as("createdAt"),
                expenses.updatedAt.as("updatedAt")
        );
    }

    @Override
    public List<ExpensesDTO> getAll(String driverId, YearMonth period) {
        //Điều kiện truy vấn: Chưa bị xóa
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.deleted.eq(false));
        //Tìm theo mã tài xế nếu tham số driverId hợp lệ
        if (driverId != null && !driverId.isBlank()) {
            builder.and(expenses.scheduleId.eq(schedule.id))
                    .and(schedule.driverId.eq(driverId));
        }
        //Tìm theo chu kỳ nếu period hợp lệ
        if (period != null) {
            Date startDate = Date.valueOf(period.atDay(1).atStartOfDay().toLocalDate());
            Date endDate = Date.valueOf(period.plusMonths(1).atDay(1).atStartOfDay().toLocalDate());
            builder.and(expenses.createdAt.between(startDate, endDate));
        }

        //Truy vấn, trả về kết quả
        return query.from(expenses, schedule)
                .where(builder)
                .select(expensesProjection())
                .fetch();
    }

    @Override
    public Optional<ExpensesDTO> getByID(String id) {
        //Truy vấn theo id và chưa bị xóa
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false));

        return Optional.ofNullable(
                query.from(expenses)
                        .where(builder)
                        .select(expensesProjection())
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false));

        return query.update(expenses)
                .where(builder)
                .set(expenses.deleted, true)
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public long approve(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false))
                .and(expenses.status.eq(0));

        return query.update(expenses)
                .where(builder)
                .set(expenses.status, 1)
                .execute();
    }
}
