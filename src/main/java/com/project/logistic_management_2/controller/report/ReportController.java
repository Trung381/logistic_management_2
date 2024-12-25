package com.project.logistic_management_2.controller.report;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.repository.report.ReportRepo;
import com.project.logistic_management_2.service.report.ReportService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/export/summary-salary-report")
    public ResponseEntity<Object> exportReportSummarySalary(
            @RequestParam String period) throws Exception {

        List<SummarySalaryDTO> summarySalaryReport = reportService.getSummarySalaryReport(period);


        if (!CollectionUtils.isEmpty(summarySalaryReport)) {
            String fileName = "SummarySalary Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(summarySalaryReport, fileName, ExportConfig.summarySalaryExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");

        }
    }

    @GetMapping("/export/detail-salary-report")
    public ResponseEntity<Object> exportReportDetailSalary(
            @RequestParam String userId,
            @RequestParam String period) throws Exception {

        // Lấy báo cáo chi tiết lương từ service
        ReportDetailSalaryDTO detailSalaryReport = reportService.getReport(userId, period);

        // Kiểm tra dữ liệu hợp lệ
        if (detailSalaryReport == null ||
                (detailSalaryReport.getSalary() == null && CollectionUtils.isEmpty(detailSalaryReport.getSchedules()))) {
            throw new Exception("No data available for the specified user and period.");
        }

        // Tên file Excel
        String fileName = "DetailSalaryReport_" + userId + "_" + period + ".xlsx";

        // Xuất file Excel
        ByteArrayInputStream in = ExcelUtils.export(List.of(detailSalaryReport), fileName, null);

        // Trả về file Excel dưới dạng ResponseEntity
        InputStreamResource inputStreamResource = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(inputStreamResource);
    }

}
