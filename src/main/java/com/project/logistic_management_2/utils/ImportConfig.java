package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.dto.request.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportConfig {

    private int sheetIndex;

    private int headerIndex;

    private int startRow;

    private Class dataClazz;

    private List<CellConfig> cellImportConfigs;


    public static final ImportConfig transactionImport;
    static{
        transactionImport = new ImportConfig();
        transactionImport.setSheetIndex(0);
        transactionImport.setHeaderIndex(0);
        transactionImport.setStartRow(1);
        transactionImport.setDataClazz(TransactionDTO.class);
        List<CellConfig> transactionImportCellConfigs = new ArrayList<>();

        transactionImportCellConfigs.add(new CellConfig(0, "refUserId"));
        transactionImportCellConfigs.add(new CellConfig(1, "customerName"));
        transactionImportCellConfigs.add(new CellConfig(2, "goodsId"));
        transactionImportCellConfigs.add(new CellConfig(3, "quantity"));
        transactionImportCellConfigs.add(new CellConfig(4, "transactionTime"));
        transactionImportCellConfigs.add(new CellConfig(5, "origin"));
        transactionImportCellConfigs.add(new CellConfig(6, "transactionTime"));
        transactionImportCellConfigs.add(new CellConfig(7, "destination"));
        transactionImportCellConfigs.add(new CellConfig(8, "image"));

        transactionImport.setCellImportConfigs(transactionImportCellConfigs);
    }

}
