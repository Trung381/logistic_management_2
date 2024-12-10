package com.project.logistic_management_2.controller.wareHouse;

import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.service.wareHouse.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wareHouse")
@RequiredArgsConstructor
public class WareHouseController {
    private final WareHouseService wareHouseService;

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllWareHouse() {
        return ResponseEntity.ok(
                BaseResponse.ok(wareHouseService.getAllWareHouses())
        );
    }
}
