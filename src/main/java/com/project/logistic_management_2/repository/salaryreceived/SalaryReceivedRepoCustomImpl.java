package com.project.logistic_management_2.repository.salaryreceived;

import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;

@Repository
public class SalaryReceivedRepoCustomImpl extends BaseRepo implements SalaryReceivedRepoCustom {
    public SalaryReceivedRepoCustomImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public SalaryReceived getSalaryReceivedByUserIdAndPeriod(String userId, String period) {
        QSalaryReceived qSalaryReceived = QSalaryReceived.salaryReceived;
        BooleanBuilder builder = new BooleanBuilder();

        if (userId != null && !userId.isEmpty()) {
            builder.and(qSalaryReceived.userId.eq(userId));
        }

        if (period != null && !period.isEmpty()) {
            builder.and(qSalaryReceived.period.eq(period));
        }

        return query.selectFrom(qSalaryReceived)
                .where(builder)
                .fetchOne();
    }


//    public SalaryReceived getSalaryReceivedForPreviousMonth(String userId) {
//        YearMonth currentPeriod = YearMonth.now(); // Lấy năm-tháng hiện tại
//        YearMonth previousPeriod = currentPeriod.minusMonths(1); // Lấy tháng trước
//        String previousPeriodString = previousPeriod.toString(); // Dạng yyyy-MM
//
//        return getSalaryReceivedByUserIdAndPeriod(userId, previousPeriodString);
//    }

    @Transactional
    public void createSalaryReceivedForAllUsers() {
        YearMonth currentPeriod = YearMonth.now();
        String period = currentPeriod.toString();

        String sql = "INSERT INTO salary_received (user_id, phone_allowance, basic_salary, period, " +
                "job_allowance, bonus, monthly_paid_leave, ot, snn, union_contribution, travel_expenses_reimbursement) " +
                "SELECT u.id, 10, 10, ?, 10, 10, 10, 10, 10, 10, 10 FROM users u";

        entityManager.createNativeQuery(sql)
                .setParameter(1, period) // Tham số ở vị trí thứ nhất
                .executeUpdate();

    }

}
