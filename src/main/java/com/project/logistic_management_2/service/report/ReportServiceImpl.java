package com.project.logistic_management_2.service.report;

import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.salarydeduction.SalaryDeductionDTO;
import com.project.logistic_management_2.dto.salaryreceived.SalaryReceivedDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.dto.user.UserReportDTO;
import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.user.UserReportMapper;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.schedule.ScheduleService;
import com.project.logistic_management_2.service.salarydeduction.SalaryDeductionService;
import com.project.logistic_management_2.service.salaryreceived.SalaryReceivedService;
import com.project.logistic_management_2.service.user.UserService;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private SalaryDeductionService salaryDeductionService;

    @Autowired
    private SalaryReceivedService salaryReceivedService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportRepo reportRepo;

//    @Override
//    public ReportDetailSalaryDTO getReport(String userId, String period){
//        return reportRepo.getReport(userId, period);
//    }

    @Override
    public ReportDetailSalaryDTO getReport(String userId, String period) {
        validateUserIdAndPeriod(userId, period);

        SalaryDeduction salaryDeduction = salaryDeductionService.getSalaryDeductionByUserIdAndPeriod(userId, period);
        SalaryReceived salaryReceived = salaryReceivedService.getSalaryReceivedByUserIdAndPeriod(userId, period);
        List<ScheduleSalaryDTO> scheduleSalaryList = scheduleService.exportScheduleSalary(userId, period);

        if (salaryDeduction == null) {
            throw new NotFoundException("Không tìm thấy thông tin salary deduction cho user: " + userId + " và period: " + period);
        }
        if (salaryReceived == null) {
            throw new NotFoundException("Không tìm thấy thông tin salary received cho user: " + userId + " và period: " + period);
        }

        SalaryDeductionDTO salaryDeductionDTO = new SalaryDeductionDTO();
        salaryDeductionDTO.setMandatoryInsurance(salaryDeduction.getMandatoryInsurance());
        salaryDeductionDTO.setTradeUnion(salaryDeduction.getTradeUnion());
        salaryDeductionDTO.setAdvance(salaryDeduction.getAdvance());
        salaryDeductionDTO.setErrorOfDriver(salaryDeduction.getErrorOfDriver());
        salaryDeductionDTO.setSnn(salaryDeduction.getSnn());

        SalaryReceivedDTO salaryReceivedDTO = new SalaryReceivedDTO();
        salaryReceivedDTO.setPhoneAllowance(salaryReceived.getPhoneAllowance());
        salaryReceivedDTO.setBasicSalary(salaryReceived.getBasicSalary());
        salaryReceivedDTO.setJobAllowance(salaryReceived.getJobAllowance());
        salaryReceivedDTO.setBonus(salaryReceived.getBonus());
        salaryReceivedDTO.setMonthlyPaidLeave(salaryReceived.getMonthlyPaidLeave());
        salaryReceivedDTO.setOt(salaryReceived.getOt());
        salaryReceivedDTO.setSnn(salaryReceived.getSnn());
        salaryReceivedDTO.setUnionContribution(salaryReceived.getUnionContribution());
        salaryReceivedDTO.setTravelExpensesReimbursement(salaryReceived.getTravelExpensesReimbursement());

        ReportDetailSalaryDTO reportDetailSalaryDTO = new ReportDetailSalaryDTO();
        reportDetailSalaryDTO.setSalaryDeduction(salaryDeductionDTO);
        reportDetailSalaryDTO.setSalaryReceived(salaryReceivedDTO);
        reportDetailSalaryDTO.setSchedules(scheduleSalaryList);

        return reportDetailSalaryDTO;
    }

    private void validateUserIdAndPeriod(String userId, String period) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("UserId không hợp lệ!");
        }

        String regex = "^(\\d{4}-(0[1-9]|1[0-2]))$";
        if (!Pattern.matches(regex, period)) {
            throw new IllegalArgumentException("Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM");
        }
    }

    public List<UserReportDTO> getSumarySalaryReport(String period) {
        validatePeriod(period);

        List<User> allUsers = userService.getAllUsers();
        List<UserReportDTO> reportList = new ArrayList<>();
        UserReportMapper mapper = new UserReportMapper();

        for (User user : allUsers) {
            String userId = user.getId();
            String name = user.getFullName();

            List<ScheduleSalaryDTO> schedules = scheduleService.exportScheduleSalary(userId, period);

            Float sumTotalSchedules = 0.0f;
            if (schedules != null && !schedules.isEmpty()) {
                for (ScheduleSalaryDTO s : schedules) {
                    if (s.getTotal() != null) {
                        sumTotalSchedules += s.getTotal();
                    }
                }
            }

            SalaryDeduction deduction = salaryDeductionService.getSalaryDeductionByUserIdAndPeriod(userId, period);
            Float sumSalaryDeduction = 0.0f;
            if (deduction != null) {
                sumSalaryDeduction += safeValue(deduction.getMandatoryInsurance());
                sumSalaryDeduction += safeValue(deduction.getTradeUnion());
                sumSalaryDeduction += safeValue(deduction.getAdvance());
                sumSalaryDeduction += safeValue(deduction.getErrorOfDriver());
                sumSalaryDeduction += safeValue(deduction.getSnn());
            }

            SalaryReceived received = salaryReceivedService.getSalaryReceivedByUserIdAndPeriod(userId, period);
            Float sumSalaryReceived = 0.0f;
            if (received != null) {
                sumSalaryReceived += safeValue(received.getPhoneAllowance());
                sumSalaryReceived += safeValue(received.getBasicSalary());
                sumSalaryReceived += safeValue(received.getJobAllowance());
                sumSalaryReceived += safeValue(received.getBonus());
                sumSalaryReceived += safeValue(received.getMonthlyPaidLeave());
                sumSalaryReceived += safeValue(received.getOt());
                sumSalaryReceived += safeValue(received.getSnn());
                sumSalaryReceived += safeValue(received.getUnionContribution());
                sumSalaryReceived += safeValue(received.getTravelExpensesReimbursement());
            }

            // Tính lương thực nhận: sumTotalSchedules + sumSalaryReceived - sumSalaryDeduction
            Float netSalary = sumTotalSchedules + sumSalaryReceived - sumSalaryDeduction;

            UserReportDTO dto = mapper.toUserReportDTO(userId, name, sumTotalSchedules, sumSalaryDeduction, sumSalaryReceived, netSalary);
            reportList.add(dto);
        }

        return reportList;
    }

    private void validatePeriod(String period) {
        String regex = "^(\\d{4}-(0[1-9]|1[0-2]))$";
        if (!Pattern.matches(regex, period)) {
            throw new IllegalArgumentException("Định dạng chu kỳ không hợp lệ! Dạng đúng: yyyy-MM");
        }
    }

    private Float safeValue(Float val) {
        return val == null ? 0.0f : val;
    }
}
