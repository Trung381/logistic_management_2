package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.goods.GoodsReportDTO;
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
    public ResponseEntity<Object> getGoodsReportByMonth(@RequestParam String period) {
        List<GoodsReportDTO> goodsReports = goodsReportService.getGoodsReport(period);
        return ResponseEntity.ok(BaseResponse.ok(goodsReports));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportGoodsReport(@RequestParam String period) throws Exception {
        List<GoodsReportDTO> goodsReports = goodsReportService.getGoodsReport(period);

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
