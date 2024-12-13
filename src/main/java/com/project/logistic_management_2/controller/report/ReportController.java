package com.project.logistic_management_2.controller.report;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final ReportRepo reportRepo;

    @GetMapping("/detail-salary-report")
    public ResponseEntity<ReportDetailSalaryDTO> getDetailSalary(@RequestParam String userId, @RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(reportService.getReport(userId, period)).getData()
        );
    }

    @GetMapping("/summary-salary-report")
    public ResponseEntity<BaseResponse<List<SummarySalaryDTO>>> getSumarySalaryReport(@RequestParam String period) {
        return ResponseEntity.ok(BaseResponse.ok(reportService.getSummarySalaryReport(period)));
    }
}
