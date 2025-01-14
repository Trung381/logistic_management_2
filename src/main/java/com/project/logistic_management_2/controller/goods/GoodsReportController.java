package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.goods.GoodsReportDTO;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.service.goods.GoodsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/goods_report")
@RequiredArgsConstructor
public class GoodsReportController {

    private final GoodsReportService goodsReportService;

    @PostMapping("/create")
    public void creatGoodsReport(@RequestParam String period) {
        goodsReportService.createGoodsReport(period);
    }

    @GetMapping("/by-month")
    public ResponseEntity<Object> getGoodsReport(@RequestParam String period) {
        List<GoodsReportDTO> goodsReports = goodsReportService.getGoodsReport(period);
        return ResponseEntity.ok(BaseResponse.ok(goodsReports));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportGoodsReport(@RequestParam String period) throws Exception {
        ExportExcelResponse exportExcelResponse = goodsReportService.exportGoodsReport(period);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}
