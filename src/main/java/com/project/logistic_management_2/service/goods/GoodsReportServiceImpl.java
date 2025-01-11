package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.goods.GoodsReportDTO;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.entity.GoodsReport;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.repository.goods.GoodsReportRepo;
import com.project.logistic_management_2.repository.transaction.TransactionRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReportServiceImpl extends BaseService implements GoodsReportService {

    private final GoodsRepo goodsRepo;
    private final TransactionRepo transactionRepo;
    private final GoodsReportRepo goodsReportRepo;
    private final PermissionType type = PermissionType.REPORTS;

    @Override
    public void createGoodsReport(String period) {
        Date fromDate = Utils.convertToDate(period);
        Date toDate = Utils.convertToDateOfNextMonth(period);

        List<Goods> goodsList = goodsRepo.findAll();
        for (Goods goods : goodsList) {
            GoodsReport goodsReportMinusMonths = goodsReportRepo.getGoodReport(goods.getId(), fromDate, toDate);
            GoodsReport goodsReportCurrentMonths = new GoodsReport();
            float inboundQuantity = transactionRepo.getQuantityByOrigin(goods.getId(), true, fromDate, toDate);
            float outboundQuantity = transactionRepo.getQuantityByOrigin(goods.getId(), false, fromDate, toDate);
            if(goodsReportMinusMonths == null) {
                goodsReportCurrentMonths.setBeginningInventory(goods.getQuantity() - inboundQuantity + outboundQuantity);
                goodsReportCurrentMonths.setEndingInventory(goods.getQuantity());
                goodsReportCurrentMonths.setGoodsId(goods.getId());
                goodsReportCurrentMonths.setCreatedAt(new Date());
                goodsReportRepo.save(goodsReportCurrentMonths);
            } else {
                goodsReportCurrentMonths.setBeginningInventory(goodsReportMinusMonths.getEndingInventory());
                goodsReportCurrentMonths.setEndingInventory(goodsReportMinusMonths.getBeginningInventory() + inboundQuantity - outboundQuantity);
                goodsReportCurrentMonths.setGoodsId(goods.getId());
                goodsReportCurrentMonths.setCreatedAt(new Date());
                goodsReportRepo.save(goodsReportCurrentMonths);
            }
        }
    }

    public List<GoodsReportDTO> getGoodsReport(String period) {
        checkPermission(type, PermissionKey.VIEW);
        Date fromDate = Utils.convertToDate(period);
        Date toDate = Utils.convertToDateOfNextMonth(period);
        return goodsReportRepo.getGoodReportDTO(fromDate, toDate);
    }
}
