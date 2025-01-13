package com.project.logistic_management_2.controller.report;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

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

    @GetMapping("/export/summary-salary-report")
    public ResponseEntity<Object> exportReportSummarySalary(
            @RequestParam String period) throws Exception {

        ExportExcelResponse exportExcelResponse = reportService.exportSummarySalaryReport(period);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @GetMapping("/export/detail-salary-report")
    public ResponseEntity<Object> exportReportDetailSalary(
            @RequestParam String userId,
            @RequestParam String period
    ) throws Exception {

        ExportExcelResponse exportExcelResponse = reportService.exportReport(period, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8))
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}
