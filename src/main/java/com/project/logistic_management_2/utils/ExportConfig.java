package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.dto.request.WarehousesDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportConfig {

    private int sheetIndex;

    private int startRow;

    private Class dataClazz;

    private List<CellConfig> cellExportConfigList;

    public static final ExportConfig transactionExport;
    static {
        transactionExport = new ExportConfig();
        transactionExport.setSheetIndex(0);
        transactionExport.setStartRow(1);
        transactionExport.setDataClazz(TransactionDTO.class);
        List<CellConfig> transactionCellConfig = new ArrayList<>();
        transactionCellConfig.add(new CellConfig(0, "id"));
        transactionCellConfig.add(new CellConfig(1, "refUserId"));
        transactionCellConfig.add(new CellConfig(2, "customerName"));
        transactionCellConfig.add(new CellConfig(3, "goodsId"));
        transactionCellConfig.add(new CellConfig(4, "quantity"));
        transactionCellConfig.add(new CellConfig(5, "transactionTime"));
        transactionCellConfig.add(new CellConfig(6, "destination"));
        transactionCellConfig.add(new CellConfig(7, "image"));
        transactionCellConfig.add(new CellConfig(8, "createdAt"));
        transactionCellConfig.add(new CellConfig(9, "updatedAt"));

        transactionExport.setCellExportConfigList(transactionCellConfig);
    }

    public static final ExportConfig goodsExport;
    static {
        goodsExport = new ExportConfig();
        goodsExport.setSheetIndex(0);
        goodsExport.setStartRow(1);
        goodsExport.setDataClazz(GoodsDTO.class);
        List<CellConfig> goodsCellConfig = new ArrayList<>();
        goodsCellConfig.add(new CellConfig(0, "id"));
        goodsCellConfig.add(new CellConfig(1, "warehouseId"));
        goodsCellConfig.add(new CellConfig(2, "name"));
        goodsCellConfig.add(new CellConfig(3, "quantity"));
        goodsCellConfig.add(new CellConfig(4, "amount"));
        goodsCellConfig.add(new CellConfig(5, "createdAt"));
        goodsCellConfig.add(new CellConfig(6, "updatedAt"));

        goodsExport.setCellExportConfigList(goodsCellConfig);
    }

    public static final ExportConfig warehouseExport;
    static {
        warehouseExport = new ExportConfig();
        warehouseExport.setSheetIndex(0);
        warehouseExport.setStartRow(1);
        warehouseExport.setDataClazz(WarehousesDTO.class);
        List<CellConfig> warehouseCellConfig = new ArrayList<>();
        warehouseCellConfig.add(new CellConfig(0, "id"));
        warehouseCellConfig.add(new CellConfig(1, "name"));
        warehouseCellConfig.add(new CellConfig(2, "note"));
        warehouseCellConfig.add(new CellConfig(3, "createdAt"));
        warehouseCellConfig.add(new CellConfig(4, "updatedAt"));

        warehouseExport.setCellExportConfigList(warehouseCellConfig);
    }

}
