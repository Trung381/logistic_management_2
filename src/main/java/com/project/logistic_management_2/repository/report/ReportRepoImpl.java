package com.project.logistic_management_2.repository.report;


import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.dto.salary.SalaryDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.enums.schedule.ScheduleType;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import static com.project.logistic_management_2.entity.QUser.user;
import static com.project.logistic_management_2.entity.QTruck.truck;
import static com.project.logistic_management_2.entity.QSchedule.schedule;
import static com.project.logistic_management_2.entity.QScheduleConfig.scheduleConfig;
import static com.project.logistic_management_2.entity.QSalary.salary;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ReportRepoImpl extends BaseRepo implements ReportRepo {
    public ReportRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public ReportDetailSalaryDTO getReport(String userId, String period) {
        YearMonth ym = YearMonth.parse(period);
        Date startDate = Date.from(ym.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(ym.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        NumberExpression<Float> minAmount = scheduleConfig.amount.min().coalesce(0F).as("minAmount");
        NumberExpression<Long> scheduleCount = schedule.id.count().coalesce(0L).as("scheduleCount");
        NumberExpression<Float> total = scheduleConfig.amount.min().multiply(schedule.id.count()).coalesce(0F).as("total");

        List<Tuple> results = query
                .select(
                        user.fullName,
                        scheduleConfig.placeA,
                        scheduleConfig.placeB,
                        minAmount,
                        scheduleCount,
                        total,
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
                )
                .from(user)
                .join(truck).on(user.id.eq(truck.driverId))
                .join(schedule).on(truck.licensePlate.eq(schedule.truckLicense))
                .join(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .leftJoin(salary).on(user.id.eq(salary.userId).and(salary.period.eq(String.valueOf(ym))))
                .where(
                        user.id.eq(userId)
                                .and(schedule.departureTime.between(startDate, endDate))
                )
                .groupBy(
                        user.fullName,
                        scheduleConfig.placeA, scheduleConfig.placeB,
                        salary.phoneAllowance, salary.basicSalary, salary.jobAllowance, salary.bonus,
                        salary.monthlyPaidLeave, salary.ot, salary.receivedSnn, salary.unionContribution,
                        salary.travelExpensesReimbursement,
                        salary.mandatoryInsurance, salary.tradeUnion, salary.advance,
                        salary.errorOfDriver, salary.deductionSnn
                )
                .fetch();

        ReportDetailSalaryDTO report = new ReportDetailSalaryDTO();
        SalaryDTO salaryDTO = null;
        List<ScheduleSalaryDTO> schedules = new ArrayList<>();

        if (!results.isEmpty()) {
            for (Tuple tuple : results) {
                if (salaryDTO == null) {
                    salaryDTO = SalaryDTO.builder()
                            .phoneAllowance(coalesce(tuple.get(salary.phoneAllowance), 0F))
                            .basicSalary(coalesce(tuple.get(salary.basicSalary), 0F))
                            .jobAllowance(coalesce(tuple.get(salary.jobAllowance), 0F))
                            .bonus(coalesce(tuple.get(salary.bonus), 0F))
                            .monthlyPaidLeave(coalesce(tuple.get(salary.monthlyPaidLeave), 0F))
                            .ot(coalesce(tuple.get(salary.ot), 0F))
                            .receivedSnn(coalesce(tuple.get(salary.receivedSnn), 0F))
                            .unionContribution(coalesce(tuple.get(salary.unionContribution), 0F))
                            .travelExpensesReimbursement(coalesce(tuple.get(salary.travelExpensesReimbursement), 0F))
                            .mandatoryInsurance(coalesce(tuple.get(salary.mandatoryInsurance), 0F))
                            .tradeUnion(coalesce(tuple.get(salary.tradeUnion), 0F))
                            .advance(coalesce(tuple.get(salary.advance), 0F))
                            .errorOfDriver(coalesce(tuple.get(salary.errorOfDriver), 0F))
                            .deductionSnn(coalesce(tuple.get(salary.deductionSnn), 0F))
                            .build();
                }

                ScheduleSalaryDTO schedule = new ScheduleSalaryDTO(
                        tuple.get(user.fullName),
                        tuple.get(scheduleConfig.placeA),
                        tuple.get(scheduleConfig.placeB),
                        coalesce(tuple.get(minAmount), 0F),
                        coalesce(tuple.get(scheduleCount), 0L).intValue(),
                        coalesce(tuple.get(total), 0F)
                );
                schedules.add(schedule);
            }
        } else {
            salaryDTO = SalaryDTO.builder().build();
        }

        report.setSalary(salaryDTO);
        report.setSchedules(schedules);

        return report;
    }

    /**
     * Phương thức hỗ trợ xử lý COALESCE
     * Trả về giá trị nếu không null, ngược lại trả về giá trị mặc định.
     */
    private <T> T coalesce(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Override
    public List<SummarySalaryDTO> getSummarySalary(String period) {
        YearMonth ym = YearMonth.parse(period);
        Date startDate = Date.from(ym.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(ym.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        BooleanBuilder builder = new BooleanBuilder()
                .and(truck.driverId.eq(user.id))
                .and(schedule.departureTime.between(startDate, endDate))
                .and(schedule.type.eq(ScheduleType.PAYROLL.getValue()))
                .and(truck.deleted.eq(false))
                .and(scheduleConfig.deleted.eq(false))
                .and(schedule.deleted.eq(false));

        Expression<Float> sumTotalSchedules = JPAExpressions
                .select(scheduleConfig.amount.sum().coalesce(0F))
                .from(truck)
                .join(schedule).on(truck.licensePlate.eq(schedule.truckLicense))
                .join(scheduleConfig).on(schedule.scheduleConfigId.eq(scheduleConfig.id))
                .where(builder);

        Expression<Float> sumSalaryDeduction = JPAExpressions
                .select(
                        salary.mandatoryInsurance
                                .add(salary.tradeUnion)
                                .add(salary.advance)
                                .add(salary.errorOfDriver)
                                .add(salary.deductionSnn)
                                .coalesce(0F)
                )
                .from(salary)
                .where(
                        salary.userId.eq(user.id)
                                .and(salary.period.eq(period))
                );

        // Subquery for sumSalaryReceived
        Expression<Float> sumSalaryReceived = JPAExpressions
                .select(
                        salary.phoneAllowance
                                .add(salary.basicSalary)
                                .add(salary.jobAllowance)
                                .add(salary.bonus)
                                .add(salary.monthlyPaidLeave)
                                .add(salary.ot)
                                .add(salary.receivedSnn)
                                .add(salary.unionContribution)
                                .add(salary.travelExpensesReimbursement)
                                .coalesce(0F)
                )
                .from(salary)
                .where(
                        salary.userId.eq(user.id)
                                .and(salary.period.eq(period))
                );

        NumberExpression<Float> netSalary = Expressions
                .numberTemplate(Float.class, "{0} - {1} + {2}",
                        sumTotalSchedules,
                        sumSalaryDeduction,
                        sumSalaryReceived
                ).coalesce(0F);

        return query
                .select(Projections.constructor(
                        SummarySalaryDTO.class,
                        user.id,
                        user.fullName,
                        sumTotalSchedules,
                        sumSalaryDeduction,
                        sumSalaryReceived,
                        netSalary
                ))
                .from(user)
                .where(user.status.eq(1)) // Chỉ lấy người dùng đang hoạt động
                .groupBy(user.id, user.fullName)
                .fetch();
    }
}

