package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.goods.GoodsReportDTO;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.entity.Goods;
import com.project.logistic_management_2.entity.GoodsReport;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.repository.goods.GoodsReportRepo;
import com.project.logistic_management_2.repository.transaction.TransactionRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.Utils;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
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
        Date[] range = Utils.createDateRange(period);

        List<Goods> goodsList = goodsRepo.findAll();
        for (Goods goods : goodsList) {
            GoodsReport goodsReportMinusMonths = goodsReportRepo.getGoodReport(goods.getId(), range[0], range[1]);
            GoodsReport goodsReportCurrentMonths = new GoodsReport();
            float inboundQuantity = transactionRepo.getQuantityByOrigin(goods.getId(), true, range[0], range[1]);
            float outboundQuantity = transactionRepo.getQuantityByOrigin(goods.getId(), false, range[0], range[1]);
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

    @Override
    public List<GoodsReportDTO> getGoodsReport(String period) {
        checkPermission(type, PermissionKey.VIEW);
        Date[] range = Utils.createDateRange(period);
        return goodsReportRepo.getGoodReportDTO(range[0], range[1]);
    }

    @Override
    public ExportExcelResponse exportGoodsReport(String period) throws Exception {
        List<GoodsReportDTO> goodsReports = getGoodsReport(period);

        if (CollectionUtils.isEmpty(goodsReports)) {
            throw new NotFoundException("No data");
        }
        String fileName = "GoodsReport Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(goodsReports, fileName, ExportConfig.goodsReportExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
