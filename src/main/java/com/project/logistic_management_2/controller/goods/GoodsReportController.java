package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import com.project.logistic_management_2.service.goods.GoodsReportService;
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
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/goods_report")
@RequiredArgsConstructor
public class GoodsReportController {

    private final GoodsReportService goodsReportService;

    @PostMapping("/create")
    public void creatGoodsReport() {
        goodsReportService.createGoodsReport();
    }

    @GetMapping("/by-month")
    public ResponseEntity<Object> getGoodsReportByMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        List<GoodsReportDTO> goodsReports = goodsReportService.getGoodsReportByYearMonth(yearMonth);
        return ResponseEntity.ok(BaseResponse.ok(goodsReports));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportGoodsReport(@RequestParam("year") int year, @RequestParam("month") int month) throws Exception {
        YearMonth yearMonth = YearMonth.of(year, month);
        List<GoodsReportDTO> goodsReports = goodsReportService.getGoodsReportByYearMonth(yearMonth);


        if (!CollectionUtils.isEmpty(goodsReports)) {
            String fileName = "GoodsReport Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(goodsReports, fileName, ExportConfig.goodsReportExport);

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
}
