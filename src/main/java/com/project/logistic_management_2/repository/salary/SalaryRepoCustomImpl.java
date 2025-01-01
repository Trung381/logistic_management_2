package com.project.logistic_management_2.repository.salary;

import com.project.logistic_management_2.dto.salary.SalaryDTO;
import com.project.logistic_management_2.dto.salary.SalaryUserDTO;
import com.project.logistic_management_2.dto.salary.UpdateSalaryDTO;
import com.project.logistic_management_2.entity.QSalary;
import com.project.logistic_management_2.entity.QUser;
import com.project.logistic_management_2.entity.Salary;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;


@Repository
public class SalaryRepoCustomImpl extends BaseRepo implements SalaryRepoCustom {

    public SalaryRepoCustomImpl(EntityManager entityManager) {
        super(entityManager);
    }

    public List<SalaryUserDTO> getAllSalaryWithUserPeriod(String period){
        QSalary s = QSalary.salary;
        QUser u = QUser.user;

        return query.
                select(Projections.fields(SalaryUserDTO.class,
                        s.id,
//                        s.userId,
                        u.fullName,
                        s.period,
                        s.phoneAllowance,
                        s.basicSalary,
                        s.jobAllowance,
                        s.bonus,
                        s.monthlyPaidLeave,
                        s.ot,
                        s.receivedSnn,
                        s.unionContribution,
                        s.travelExpensesReimbursement,
                        s.mandatoryInsurance,
                        s.tradeUnion,
                        s.advance,
                        s.errorOfDriver,
                        s.deductionSnn
                ))
                .from(s)
                .join(u).on(s.userId.eq(u.id))
                .where(s.period.eq(period))
                .fetch();
    }

    public SalaryUserDTO getSalaryWithUser(Integer id){
        QSalary s = QSalary.salary;
        QUser u = QUser.user;

        return query.
                select(Projections.fields(SalaryUserDTO.class,
                        s.id,
//                        s.userId,
                        u.fullName,
                        s.period,
                        s.phoneAllowance,
                        s.basicSalary,
                        s.jobAllowance,
                        s.bonus,
                        s.monthlyPaidLeave,
                        s.ot,
                        s.receivedSnn,
                        s.unionContribution,
                        s.travelExpensesReimbursement,
                        s.mandatoryInsurance,
                        s.tradeUnion,
                        s.advance,
                        s.errorOfDriver,
                        s.deductionSnn
                ))
                .from(s)
                .join(u).on(s.userId.eq(u.id))
                .where(s.id.eq(id))
                .fetchOne();
    }

//    @Transactional
//    public Boolean updateSalary(Integer id, UpdateSalaryDTO updateSalaryDTO){
//        QSalary s = QSalary.salary;
//        long rowsAffected = query.update(s)
//                .where(s.id.eq(id))
//                .set(s.phoneAllowance, updateSalaryDTO.getPhoneAllowance())
//                .set( s.basicSalary, updateSalaryDTO.getBasicSalary())
//                .set(s.jobAllowance, updateSalaryDTO.getJobAllowance())
//                .set(s.bonus, updateSalaryDTO.getBonus())
//                .set(s.monthlyPaidLeave,  updateSalaryDTO.getMonthlyPaidLeave())
//                .set(s.ot, updateSalaryDTO.getOt())
//                .set(s.receivedSnn, updateSalaryDTO.getReceivedSnn())
//                .set(s.unionContribution, updateSalaryDTO.getUnionContribution())
//                .set(s.travelExpensesReimbursement, updateSalaryDTO.getTravelExpensesReimbursement())
//                .set(s.mandatoryInsurance, updateSalaryDTO.getBasicSalary()*0.105F)
//                .set(s.tradeUnion, updateSalaryDTO.getBasicSalary()*0.01F)
//                .set(s.advance, updateSalaryDTO.getAdvance())
//                .set(s.errorOfDriver, updateSalaryDTO.getErrorOfDriver())
//                .set(s.deductionSnn, updateSalaryDTO.getDeductionSnn())
//                .execute();
//
//        return (rowsAffected > 0) ? true : false;
//    }
    @Transactional
    public Boolean updateSalary(Integer id, UpdateSalaryDTO updateSalaryDTO) {
        QSalary s = QSalary.salary;
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(s.id.eq(id));

        JPAUpdateClause updateClause = query.update(s).where(whereClause);

        if (updateSalaryDTO.getPhoneAllowance() != null) {
            updateClause.set(s.phoneAllowance, updateSalaryDTO.getPhoneAllowance());
        }
        if (updateSalaryDTO.getBasicSalary() != null) {
            updateClause.set(s.basicSalary, updateSalaryDTO.getBasicSalary());
            updateClause.set(s.mandatoryInsurance, updateSalaryDTO.getBasicSalary() * 0.105F);
            updateClause.set(s.tradeUnion, updateSalaryDTO.getBasicSalary() * 0.01F);
        }
        if (updateSalaryDTO.getJobAllowance() != null) {
            updateClause.set(s.jobAllowance, updateSalaryDTO.getJobAllowance());
        }
        if (updateSalaryDTO.getBonus() != null) {
            updateClause.set(s.bonus, updateSalaryDTO.getBonus());
        }
        if (updateSalaryDTO.getMonthlyPaidLeave() != null) {
            updateClause.set(s.monthlyPaidLeave, updateSalaryDTO.getMonthlyPaidLeave());
        }
        if (updateSalaryDTO.getOt() != null) {
            updateClause.set(s.ot, updateSalaryDTO.getOt());
        }
        if (updateSalaryDTO.getReceivedSnn() != null) {
            updateClause.set(s.receivedSnn, updateSalaryDTO.getReceivedSnn());
        }
        if (updateSalaryDTO.getUnionContribution() != null) {
            updateClause.set(s.unionContribution, updateSalaryDTO.getUnionContribution());
        }
        if (updateSalaryDTO.getTravelExpensesReimbursement() != null) {
            updateClause.set(s.travelExpensesReimbursement, updateSalaryDTO.getTravelExpensesReimbursement());
        }
        if (updateSalaryDTO.getAdvance() != null) {
            updateClause.set(s.advance, updateSalaryDTO.getAdvance());
        }
        if (updateSalaryDTO.getErrorOfDriver() != null) {
            updateClause.set(s.errorOfDriver, updateSalaryDTO.getErrorOfDriver());
        }
        if (updateSalaryDTO.getDeductionSnn() != null) {
            updateClause.set(s.deductionSnn, updateSalaryDTO.getDeductionSnn());
        }

        return updateClause.execute() > 0;
    }

    public void deleteSalary(Integer salaryId) {
        query.delete(QSalary.salary)
                .where(QSalary.salary.id.eq(salaryId))
                .execute();
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
