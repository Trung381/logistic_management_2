package com.project.logistic_management_2.repository.salary;

import com.project.logistic_management_2.dto.salary.SalaryUserDTO;
import com.project.logistic_management_2.dto.salary.UpdateSalaryDTO;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

import static com.project.logistic_management_2.entity.salary.QSalary.salary;
import static com.project.logistic_management_2.entity.user.QUser.user;

@Repository
public class SalaryRepoCustomImpl extends BaseRepo implements SalaryRepoCustom {

    public SalaryRepoCustomImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private final Expression<SalaryUserDTO> expression = Projections.fields(SalaryUserDTO.class,
            salary.id,
            user.fullName,
            salary.period,
            salary.phoneAllowance,
            salary.basicSalary,
            salary.jobAllowance,
            salary.bonus,
            salary.monthlyPaidLeave,
            salary.ot,
            salary.receivedSnn,
            salary.unionContribution,
            salary.travelExpensesReimbursement,
            salary.mandatoryInsurance,
            salary.tradeUnion,
            salary.advance,
            salary.errorOfDriver,
            salary.deductionSnn
    );

    public List<SalaryUserDTO> getAllSalaryWithUserPeriod(String period){
        return query.select(expression)
                .from(salary)
                .join(user).on(salary.userId.eq(user.id))
                .where(salary.period.eq(period))
                .fetch();
    }

    public SalaryUserDTO getSalaryWithUser(Integer id){
        return query.select(expression)
                .from(salary)
                .join(user).on(salary.userId.eq(user.id))
                .where(salary.id.eq(id))
                .fetchOne();
    }

    @Transactional
    public Boolean updateSalary(Integer id, UpdateSalaryDTO updateSalaryDTO) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(salary.id.eq(id));

        JPAUpdateClause updateClause = query.update(salary).where(whereClause);

        if (updateSalaryDTO.getPhoneAllowance() != null) {
            updateClause.set(salary.phoneAllowance, updateSalaryDTO.getPhoneAllowance());
        }
        if (updateSalaryDTO.getBasicSalary() != null) {
            updateClause.set(salary.basicSalary, updateSalaryDTO.getBasicSalary());
            updateClause.set(salary.mandatoryInsurance, updateSalaryDTO.getBasicSalary() * 0.105F);
            updateClause.set(salary.tradeUnion, updateSalaryDTO.getBasicSalary() * 0.01F);
        }
        if (updateSalaryDTO.getJobAllowance() != null) {
            updateClause.set(salary.jobAllowance, updateSalaryDTO.getJobAllowance());
        }
        if (updateSalaryDTO.getBonus() != null) {
            updateClause.set(salary.bonus, updateSalaryDTO.getBonus());
        }
        if (updateSalaryDTO.getMonthlyPaidLeave() != null) {
            updateClause.set(salary.monthlyPaidLeave, updateSalaryDTO.getMonthlyPaidLeave());
        }
        if (updateSalaryDTO.getOt() != null) {
            updateClause.set(salary.ot, updateSalaryDTO.getOt());
        }
        if (updateSalaryDTO.getReceivedSnn() != null) {
            updateClause.set(salary.receivedSnn, updateSalaryDTO.getReceivedSnn());
        }
        if (updateSalaryDTO.getUnionContribution() != null) {
            updateClause.set(salary.unionContribution, updateSalaryDTO.getUnionContribution());
        }
        if (updateSalaryDTO.getTravelExpensesReimbursement() != null) {
            updateClause.set(salary.travelExpensesReimbursement, updateSalaryDTO.getTravelExpensesReimbursement());
        }
        if (updateSalaryDTO.getAdvance() != null) {
            updateClause.set(salary.advance, updateSalaryDTO.getAdvance());
        }
        if (updateSalaryDTO.getErrorOfDriver() != null) {
            updateClause.set(salary.errorOfDriver, updateSalaryDTO.getErrorOfDriver());
        }
        if (updateSalaryDTO.getDeductionSnn() != null) {
            updateClause.set(salary.deductionSnn, updateSalaryDTO.getDeductionSnn());
        }

        return updateClause.execute() > 0;
    }

    @Transactional
    public void createSalaryForAllUsers() {
        YearMonth currentPeriod = YearMonth.now();
        String period = currentPeriod.toString();

        String sql = "INSERT INTO salary (user_id, period, phone_allowance, basic_salary, job_allowance, bonus, " +
                "monthly_paid_leave, ot, received_snn, union_contribution, travel_expenses_reimbursement, " +
                "mandatory_insurance, trade_union, advance, error_of_driver, deduction_snn) " +
                "SELECT u.id, ?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 FROM users u";

        entityManager.createNativeQuery(sql)
                .setParameter(1, period)
                .executeUpdate();
    }
}
