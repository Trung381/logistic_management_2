package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.service.goods.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    @GetMapping("/filter")
    public ResponseEntity<Object> getGoodsByFilter(@RequestParam(required = false) String warehouseId) {
        return ResponseEntity.ok(
                BaseResponse.ok(goodsService.getGoodsByFilter(warehouseId))
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportGoods(@RequestParam(required = false) String warehouseId) throws Exception {

        ExportExcelResponse exportExcelResponse = goodsService.exportGoods(warehouseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}
