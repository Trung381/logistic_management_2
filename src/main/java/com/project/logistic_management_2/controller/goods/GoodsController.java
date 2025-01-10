package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.service.goods.GoodsService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        List<GoodsDTO> goods = goodsService.getGoodsByFilter(warehouseId);


        if (!CollectionUtils.isEmpty(goods)) {
            String fileName = "Goods Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(goods, fileName, ExportConfig.goodsExport);

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
