package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.entity.GoodsReport;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.repository.goods.GoodsReportRepo;
import com.project.logistic_management_2.repository.transaction.TransactionRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
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
    @Scheduled(cron = "0 0 0 1 * *")
    public void createGoodsReport() {
        YearMonth currentMonth = YearMonth.now().minusMonths(1); // Tháng hiện tại
        YearMonth previousMonth = currentMonth.minusMonths(2);

        List<Goods> goodsList = goodsRepo.findAll();
        for (Goods goods : goodsList) {
            GoodsReport goodsReportMinusMonths = goodsReportRepo.getGoodReportByYearMonth(goods.getId(), previousMonth);
            GoodsReport goodsReportCurrentMonths = new GoodsReport();
            float inboundQuantity = transactionRepo.getQuantityByOrigin(goods.getId(), true, currentMonth);
            float outboundQuantity = transactionRepo.getQuantityByOrigin(goods.getId(), false, currentMonth);
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

    public List<GoodsReportDTO> getGoodsReportByYearMonth(YearMonth yearMonth) {

        checkPermission(type, PermissionKey.VIEW);

        return goodsReportRepo.getGoodReportDTOByYearMonth(yearMonth);
    }
}
