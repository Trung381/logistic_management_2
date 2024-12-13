package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import com.project.logistic_management_2.service.goods.GoodsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
