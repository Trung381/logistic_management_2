package com.project.logistic_management_2.repository.report;


import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.salarydeduction.SalaryDeductionDTO;
import com.project.logistic_management_2.dto.salaryreceived.SalaryReceivedDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import com.project.logistic_management_2.entity.*;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.project.logistic_management_2.entity.QSalaryDeduction.salaryDeduction;
import static com.project.logistic_management_2.entity.QSalaryReceived.salaryReceived;
import static com.project.logistic_management_2.entity.QSchedule.schedule;
import static com.project.logistic_management_2.entity.QScheduleConfig.scheduleConfig;
import static com.project.logistic_management_2.entity.QTruck.truck;
import static com.project.logistic_management_2.entity.QUser.user;

@Repository
public class ReportRepoImpl extends BaseRepo implements ReportRepo {
    public ReportRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public ReportDetailSalaryDTO getReport(String userId, String period) {
        return null;
    }


}
