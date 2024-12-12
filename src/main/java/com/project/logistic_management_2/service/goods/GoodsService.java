package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService {
    List<GoodsDTO> getAllGoods();
}