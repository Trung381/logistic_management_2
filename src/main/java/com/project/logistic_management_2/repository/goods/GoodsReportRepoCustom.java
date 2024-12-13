package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import com.project.logistic_management_2.entity.GoodsReport;

import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.List;

public interface GoodsReportRepoCustom {
    GoodsReport getGoodReportByYearMonth(String goodsId , YearMonth yearMonth);
    List<GoodsReportDTO> getGoodReportDTOByYearMonth(YearMonth yearMonth);
}
