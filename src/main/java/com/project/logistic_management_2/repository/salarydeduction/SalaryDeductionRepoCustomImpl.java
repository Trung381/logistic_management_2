package com.project.logistic_management_2.repository.salarydeduction;

import com.project.logistic_management_2.entity.QSalaryDeduction;
import com.project.logistic_management_2.entity.SalaryDeduction;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;

@Repository
public class SalaryDeductionRepoCustomImpl extends BaseRepo implements SalaryDeductionRepoCustom{
    public SalaryDeductionRepoCustomImpl(EntityManager entityManager) {
        super(entityManager);
    }


    @Override
    public SalaryDeduction getSalaryDeductionByUserIdAndPeriod(String userId, String period) {
        QSalaryDeduction qSalaryDeduction = QSalaryDeduction.salaryDeduction;
        BooleanBuilder builder = new BooleanBuilder();

        // Thêm điều kiện userId nếu không null hoặc rỗng
        if (userId != null && !userId.isEmpty()) {
            builder.and(qSalaryDeduction.userId.eq(userId));
        }

        // Thêm điều kiện period nếu không null hoặc rỗng
        if (period != null && !period.isEmpty()) {
            builder.and(qSalaryDeduction.period.eq(period));
        }

        // Thực hiện truy vấn
        return query.selectFrom(qSalaryDeduction)
                .where(builder)
                .fetchOne();
    }

    @Transactional
    public void createSalaryDeductionForAllUsers() {
        YearMonth currentPeriod = YearMonth.now();
        String period = currentPeriod.toString();

        String sql = "INSERT INTO salary_deduction (user_id, period, advance, error_of_driver, mandatory_insurance, snn, trade_union) " +
                "SELECT u.id, ?, 11, 11, 11, 11, 11 FROM users u";

        entityManager.createNativeQuery(sql)
                .setParameter(1, period) // Tham số ở vị trí thứ nhất
                .executeUpdate();

    }
}
