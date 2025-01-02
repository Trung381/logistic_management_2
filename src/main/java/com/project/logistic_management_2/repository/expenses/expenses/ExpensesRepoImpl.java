package com.project.logistic_management_2.repository.expenses.expenses;

import com.project.logistic_management_2.dto.expenses.*;

import static com.project.logistic_management_2.entity.QExpenses.expenses;
import static com.project.logistic_management_2.entity.QExpensesConfig.expensesConfig;
import static com.project.logistic_management_2.entity.QSchedule.schedule;
import static com.project.logistic_management_2.entity.QTruck.truck;
import static com.project.logistic_management_2.entity.QUser.user;
import static com.project.logistic_management_2.entity.QExpenseAdvances.expenseAdvances;

import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
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
                user.id.as("driverId"),
                user.fullName.as("driverName"),
                //Thông tin cấu hình chi phí: mã, loại chi phí
                expenses.expensesConfigId.as("expensesConfigId"),
                JPAExpressions.select(expensesConfig.type.as("expensesConfigType"))
                        .from(expensesConfig)
                        .where(expensesConfig.id.eq(expenses.expensesConfigId)),
                expenses.amount.as("amount"),   //Giá tiền
                expenses.note.as("note"),       //Ghi chú
                expenses.imgPath.as("imgPath"), //Đường dẫn ảnh đính kèm
                //Mã lịch trình
                expenses.scheduleId.as("scheduleId"),
                expenses.status.as("status"),   //Trạng thái chi phí
                expenses.createdAt.as("createdAt"),
                expenses.updatedAt.as("updatedAt")
        );
    }

    @Override
    public List<ExpensesDTO> getAll(String expensesConfigId, String truckLicense, Timestamp fromDate, Timestamp toDate) {
        //Điều kiện truy vấn: Chưa bị xóa
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.deleted.eq(false));

        //Tìm theo loại chi phí nếu tham số expensesConfigId hợp lệ
        if (expensesConfigId != null) {
            builder.and(expenses.expensesConfigId.eq(expensesConfigId));
        }

        //Tìm theo biển số xe nếu tham số truckLicense hợp lệ
        if (truckLicense != null && !truckLicense.isBlank()) {
            builder.and(schedule.truckLicense.eq(truckLicense));
        }

        if (fromDate != null && toDate != null) {
            builder.and(expenses.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(expenses.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(expenses.createdAt.loe(toDate));
        }

        //Truy vấn, trả về kết quả
        return query.from(expenses)
                .innerJoin(schedule).on(expenses.scheduleId.eq(schedule.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(builder)
                .select(expensesProjection())
                .fetch();
    }

    @Override
    public List<ExpensesIncurredDTO> getByFilter(String driverId, YearMonth period) {
        //Điều kiện truy vấn: Chưa bị xóa
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.deleted.eq(false));

        //Tìm theo mã tài xế nếu tham số driverId hợp lệ
        if (driverId != null && !driverId.isBlank()) {
            builder.and(truck.driverId.eq(driverId));
        }
        //Tìm theo chu kỳ nếu period hợp lệ
        if (period != null) {
            Date startDate = Date.valueOf(period.atDay(1).atStartOfDay().toLocalDate());
            Date endDate = Date.valueOf(period.plusMonths(1).atDay(1).atStartOfDay().toLocalDate());
            builder.and(expenses.createdAt.between(startDate, endDate));
        }

        ConstructorExpression<ExpensesIncurredDTO> expensesIncurredExpression = Projections.constructor(
                ExpensesIncurredDTO.class,
                expenses.expensesConfigId.as("expensesConfigId"),
                expensesConfig.type.as("type"),
                expenses.amount.sum().as("amount")
        );
        return query.from(expenses)
                .innerJoin(expensesConfig).on(expenses.expensesConfigId.eq(expensesConfig.id))
                .innerJoin(schedule).on(expenses.scheduleId.eq(schedule.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .where(builder)
                .select(expensesIncurredExpression)
                .groupBy(expenses.expensesConfigId, expensesConfig.type)
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
                        .innerJoin(schedule).on(expenses.scheduleId.eq(schedule.id))
                        .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                        .innerJoin(user).on(truck.driverId.eq(user.id))
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

    @Override
    public List<ExpensesReportDTO> reportForAll(String period) {
        //Truy vấn chi phí phát sinh: ExpensesIncurredDTO
        ConstructorExpression<ExpensesIncurredDTO> expensesIncurredExpression = Projections.constructor(
                ExpensesIncurredDTO.class,
                expensesConfig.id.as("expensesConfigId"),   //ID loại chi phí
                expensesConfig.type.as("type"),             //Loại chi phí
                expenses.amount.sum().as("amount")          //Tổng tiền của loại tương ứng
        );

        String prevPeriod = prevPeriod(period);

        //Truy vấn thông tin cơ bản của báo cáo chi phí: ExpensesReportDTO
        ConstructorExpression<ExpensesReportDTO> reportExpression = Projections.constructor(
                ExpensesReportDTO.class,
                user.id.as("driverId"),                 //ID tài xế
                user.fullName.as("driverName"),         //Tên tài xế
                //Danh sách biển số xe (Dạng String)
                Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", schedule.truckLicense).as("truckLicense"),
                //Danh sách biển số rơ-mooc (String)
                Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", schedule.moocLicense).as("moocLicense"),
                //Số dư từ chu kỳ ứng trước
                JPAExpressions.select(expenseAdvances.remainingBalance.coalesce(0f).as("prevRemainingBalance"))
                        .from(expenseAdvances)
                        .where(expenseAdvances.driverId.eq(user.id).and(expenseAdvances.period.eq(prevPeriod))),
                expenseAdvances.advance.coalesce(0f).as("advance"),                   //Tiền ứng trong chu kỳ
                expenseAdvances.remainingBalance.coalesce(0f).as("remainingBalance")  //Số dư kỳ hiện tại (kỳ đang truy vấn), mặc định null cho đến cuối kỳ
        );

        List<ExpensesReportDTO> reports = reportsQuery(period, reportExpression);

        //Truy vấn danh sách chi phí phát sinh cho từng tài xế
        for (ExpensesReportDTO report : reports) {
            report.setExpensesIncurred(
                    expensesIncurredEachDriverQuery(report.getDriverId(), expensesIncurredExpression)
            );
        }

        return reports;
    }

    @Override
    public long countByID(String id) {
        //conditions: exist and has not been deleted
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false));

        Long res = query.from(expenses)
                .where(builder)
                .select(expenses.id.count().coalesce(0L))
                .fetchOne();

        return res != null ? res : 0;
    }

    /**
     * Return true if this expenses has not approved yet
     */
    @Override
    public boolean checkApproved(String id) {
        // exist, has not been deleted and approved
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenses.id.eq(id))
                .and(expenses.deleted.eq(false))
                .and(expenses.status.eq(0));

        Long res = query.from(expenses)
                .where(builder)
                .select(expenses.id.count().coalesce(0L))
                .fetchOne();
        return res != null && res > 0;
    }

    //Tính và trả về chu kỳ trước
    private String prevPeriod(String period) {
        YearMonth yearMonth = YearMonth.parse(period);
        YearMonth prevMonth = yearMonth.minusMonths(1);
        return prevMonth.toString();
    }

    private List<ExpensesReportDTO> reportsQuery(String period, ConstructorExpression<ExpensesReportDTO> expression) {
        //Điều kiện truy vấn
        BooleanBuilder builder = new BooleanBuilder()
                .and(user.roleId.eq(4))                //Tài xế
                .and(expenseAdvances.period.eq(period));    //Chu kỳ truy vấn

        //Danh sách báo cáo, gồm các thông tin cơ bản không bao gồm danh sách chi phí phát sinh của từng tài xế
        return query.from(user)
                .leftJoin(truck).on(truck.driverId.eq(user.id))
                .leftJoin(schedule).on(truck.licensePlate.eq(schedule.truckLicense))
                .leftJoin(expenseAdvances).on(expenseAdvances.driverId.eq(user.id))
                .where(builder)
                .select(expression)
                .groupBy(user.id, user.fullName, expenseAdvances.advance, expenseAdvances.remainingBalance)
                .fetch();
    }

    private List<ExpensesIncurredDTO> expensesIncurredEachDriverQuery(String driverId, ConstructorExpression<ExpensesIncurredDTO> expression) {
        return query.from(expenses)
                .innerJoin(expensesConfig).on(expenses.expensesConfigId.eq(expensesConfig.id))
                .innerJoin(schedule).on(expenses.scheduleId.eq(schedule.id))
                .innerJoin(truck).on(schedule.truckLicense.eq(truck.licensePlate))
                .select(expression)
                .where(truck.driverId.eq(driverId))
                .groupBy(expensesConfig.id, expensesConfig.type)
                .fetch();
    }
}
