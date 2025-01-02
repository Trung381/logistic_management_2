package com.project.logistic_management_2.repository.report;


import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.dto.salary.SalaryDTO;
import com.project.logistic_management_2.dto.salarydeduction.SalaryDeductionDTO;
import com.project.logistic_management_2.dto.salaryreceived.SalaryReceivedDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;


import java.time.LocalDate;
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
        // Chuyển đổi period thành YearMonth
        YearMonth ym = YearMonth.parse(period);
        Date startDate = Date.from(ym.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(ym.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        // Khởi tạo các Q-class
        QUser u = QUser.user;
        QTruck t = QTruck.truck;
        QSchedule s = QSchedule.schedule;
        QScheduleConfig sc = QScheduleConfig.scheduleConfig;
        QSalary salary = QSalary.salary; // Bảng salary mới

        // Định nghĩa các biểu thức tổng hợp
        NumberExpression<Float> minAmount = sc.amount.min().coalesce(0F).as("minAmount");
        NumberExpression<Long> scheduleCount = s.id.count().coalesce(0L).as("scheduleCount");
        NumberExpression<Float> total = sc.amount.min().multiply(s.id.count()).coalesce(0F).as("total");

        System.err.println(period);

        // Thực hiện truy vấn
        List<Tuple> results = query
                .select(
//                        u.id,
                        u.fullName,
                        sc.placeA,
                        sc.placeB,
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
                .from(u)
                .join(t).on(u.id.eq(t.driverId))
                .join(s).on(t.licensePlate.eq(s.truckLicense))
                .join(sc).on(s.scheduleConfigId.eq(sc.id))
                .leftJoin(salary).on(u.id.eq(salary.userId).and(salary.period.eq(String.valueOf(ym))))
                .where(
                        u.id.eq(userId)
//                                .and(s.arrivalTime.between(startDate, endDate))
                                .and(s.departureTime.between(startDate, endDate))
//                                .and(s.createdAt.between(startDate, endDate))
                )
                .groupBy(
//                        u.id, // Thêm driverId vào groupBy
                        u.fullName,
                        sc.placeA, sc.placeB,
                        salary.phoneAllowance, salary.basicSalary, salary.jobAllowance, salary.bonus,
                        salary.monthlyPaidLeave, salary.ot, salary.receivedSnn, salary.unionContribution,
                        salary.travelExpensesReimbursement,
                        salary.mandatoryInsurance, salary.tradeUnion, salary.advance,
                        salary.errorOfDriver, salary.deductionSnn
                )
                .fetch();

        // Khởi tạo DTOs
        ReportDetailSalaryDTO report = new ReportDetailSalaryDTO();
        SalaryDTO salaryDTO = null;
        List<ScheduleSalaryDTO> schedules = new ArrayList<>();

        // Xử lý kết quả truy vấn
        if (!results.isEmpty()) {
            for (Tuple tuple : results) {
                if (salaryDTO == null) {
                    // Khởi tạo SalaryDTO từ tuple đầu tiên
                    salaryDTO = new SalaryDTO();
                    salaryDTO.setPhoneAllowance(coalesce(tuple.get(salary.phoneAllowance), 0F));
                    salaryDTO.setBasicSalary(coalesce(tuple.get(salary.basicSalary), 0F));
                    salaryDTO.setJobAllowance(coalesce(tuple.get(salary.jobAllowance), 0F));
                    salaryDTO.setBonus(coalesce(tuple.get(salary.bonus), 0F));
                    salaryDTO.setMonthlyPaidLeave(coalesce(tuple.get(salary.monthlyPaidLeave), 0F));
                    salaryDTO.setOt(coalesce(tuple.get(salary.ot), 0F));
                    salaryDTO.setReceivedSnn(coalesce(tuple.get(salary.receivedSnn), 0F));
                    salaryDTO.setUnionContribution(coalesce(tuple.get(salary.unionContribution), 0F));
                    salaryDTO.setTravelExpensesReimbursement(coalesce(tuple.get(salary.travelExpensesReimbursement), 0F));
                    salaryDTO.setMandatoryInsurance(coalesce(tuple.get(salary.mandatoryInsurance), 0F));
                    salaryDTO.setTradeUnion(coalesce(tuple.get(salary.tradeUnion), 0F));
                    salaryDTO.setAdvance(coalesce(tuple.get(salary.advance), 0F));
                    salaryDTO.setErrorOfDriver(coalesce(tuple.get(salary.errorOfDriver), 0F));
                    salaryDTO.setDeductionSnn(coalesce(tuple.get(salary.deductionSnn), 0F));
                }

                ScheduleSalaryDTO schedule = new ScheduleSalaryDTO(
                    tuple.get(u.fullName),
                    tuple.get(sc.placeA),
                    tuple.get(sc.placeB),
                    coalesce(tuple.get(minAmount), 0F),
                    coalesce(tuple.get(scheduleCount), 0L).intValue(),
                    coalesce(tuple.get(total), 0F)
            );
            schedules.add(schedule);
            }
        } else {
            salaryDTO = new SalaryDTO();
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
        QUser user = QUser.user;
        QTruck truck = QTruck.truck;
        QSchedule schedule = QSchedule.schedule;
        QScheduleConfig sc = QScheduleConfig.scheduleConfig;
        QSalary salary = QSalary.salary; // Bảng salary mới

        YearMonth ym = YearMonth.parse(period);
        Date startDate = Date.from(ym.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(ym.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Expression<Float> sumTotalSchedules = JPAExpressions
                .select(sc.amount.sum().coalesce(0F))
                .from(truck)
                .join(schedule).on(truck.licensePlate.eq(schedule.truckLicense))
                .join(sc).on(schedule.scheduleConfigId.eq(sc.id))
                .where(
                        truck.driverId.eq(user.id)
                                .and(schedule.departureTime.between(startDate, endDate))
                                .and(schedule.type.eq(1)) // Loại lịch trình: 1 - Tính lương
                                .and(truck.deleted.eq(false))
                                .and(sc.deleted.eq(false))
                                .and(schedule.deleted.eq(false))
                );

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
                .groupBy(
                        user.id, user.fullName
                )
                .fetch();
    }


}

