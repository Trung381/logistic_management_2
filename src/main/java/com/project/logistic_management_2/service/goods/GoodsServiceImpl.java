package com.project.logistic_management_2.service.goods;

import com.project.logistic_management_2.dto.goods.GoodsDTO;
import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.NotFoundException;
import com.project.logistic_management_2.repository.goods.GoodsRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends BaseService implements GoodsService {

    private final GoodsRepo repository;
    private final PermissionType type = PermissionType.GOODS;

    @Override
    public List<GoodsDTO> getGoodsByFilter(String warehouseId) {
        checkPermission(type, PermissionKey.VIEW);
        return repository.getGoodsByFilter(warehouseId);
    }

    @Override
    public ExportExcelResponse exportGoods(String warehouseId) throws Exception {
        List<GoodsDTO> goods = repository.getGoodsByFilter(warehouseId);

        if (CollectionUtils.isEmpty(goods)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Goods Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(goods, fileName, ExportConfig.goodsExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
