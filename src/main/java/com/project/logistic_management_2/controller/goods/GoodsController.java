package com.project.logistic_management_2.controller.goods;

import com.project.logistic_management_2.dto.response.BaseResponse;
import com.project.logistic_management_2.service.goods.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllGoods() {
        return ResponseEntity.ok(
                BaseResponse.ok(goodsService.getAllGoods())
        );
    }
}
