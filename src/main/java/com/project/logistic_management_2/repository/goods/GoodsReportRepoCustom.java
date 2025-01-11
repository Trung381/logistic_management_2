package com.project.logistic_management_2.repository.goods;

import com.project.logistic_management_2.dto.goods.GoodsReportDTO;
import com.project.logistic_management_2.entity.GoodsReport;

import java.util.Date;
import java.util.List;

public interface GoodsReportRepoCustom {
    GoodsReport getGoodReport(String goodsId , Date fromDate, Date toDate);
    List<GoodsReportDTO> getGoodReportDTO(Date fromDate, Date toDate);
}
