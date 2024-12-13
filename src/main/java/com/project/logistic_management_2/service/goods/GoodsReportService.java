package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public interface GoodsReportService {
    void createGoodsReport();
    List<GoodsReportDTO> getGoodsReportByYearMonth(YearMonth yearMonth);
}
