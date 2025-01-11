package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.goods.GoodsReportDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsReportService {
    void createGoodsReport(String period);
    List<GoodsReportDTO> getGoodsReport(String period);
}
