package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.annotations.ExportColumn;
import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesIncurredDTO;
import com.project.logistic_management_2.dto.report.SummarySalaryDTO;
import com.project.logistic_management_2.dto.request.GoodsDTO;
import com.project.logistic_management_2.dto.request.GoodsReportDTO;
import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.dto.request.WarehousesDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.ScheduleConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
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

//    public static final ExportConfig transactionExport;
//    static {
//        transactionExport = new ExportConfig();
//        transactionExport.setSheetIndex(0);
//        transactionExport.setStartRow(1);
//        transactionExport.setDataClazz(TransactionDTO.class);
//        List<CellConfig> transactionCellConfig = new ArrayList<>();
//        transactionCellConfig.add(new CellConfig(0, "id"));
//        transactionCellConfig.add(new CellConfig(1, "fullNameRefUser"));
//        transactionCellConfig.add(new CellConfig(2, "customerName"));
//        transactionCellConfig.add(new CellConfig(3, "goodsName"));
//        transactionCellConfig.add(new CellConfig(4, "quantity"));
//        transactionCellConfig.add(new CellConfig(5, "transactionTime"));
//        transactionCellConfig.add(new CellConfig(6, "originDescription"));
//        transactionCellConfig.add(new CellConfig(7, "destination"));
//        transactionCellConfig.add(new CellConfig(8, "image"));
//        transactionCellConfig.add(new CellConfig(9, "createdAt"));
//        transactionCellConfig.add(new CellConfig(10, "updatedAt"));
//
//        transactionExport.setCellExportConfigList(transactionCellConfig);
//    }

    public static ExportConfig createExportConfig(Class<?> dataClazz, int sheetIndex, int startRow) {
        ExportConfig exportConfig = new ExportConfig();
        exportConfig.setSheetIndex(sheetIndex);
        exportConfig.setStartRow(startRow);
        exportConfig.setDataClazz(dataClazz);

        List<CellConfig> cellConfigList = new ArrayList<>();
        Field[] fields = dataClazz.getDeclaredFields();

        int columnIndex = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExportColumn.class)) {
                ExportColumn exportColumn = field.getAnnotation(ExportColumn.class);
                cellConfigList.add(new CellConfig(columnIndex++, exportColumn.name(),  field.getName()));
            }
        }

        exportConfig.setCellExportConfigList(cellConfigList);
        return exportConfig;
    }

    public static final ExportConfig transactionExport = createExportConfig(TransactionDTO.class, 0, 1);
    public static final ExportConfig goodsExport = createExportConfig(GoodsDTO.class, 0, 1);
    public static final ExportConfig goodsReportExport = createExportConfig(GoodsReportDTO.class, 0, 1);
    public static final ExportConfig warehouseExport = createExportConfig(WarehousesDTO.class, 0, 1);
    public static final ExportConfig expensesExport = createExportConfig(ExpensesDTO.class, 0, 1);
    public static final ExportConfig expensesConfigExport = createExportConfig(ExpensesConfigDTO.class, 0, 1);
    public static final ExportConfig summarySalaryExport = createExportConfig(SummarySalaryDTO.class, 0, 1);
    public static final ExportConfig scheduleConfigExport = createExportConfig(ScheduleConfig.class, 0, 1);
    public static final ExportConfig scheduleExport = createExportConfig(ScheduleDTO.class, 0, 1);
    public static final ExportConfig truckExport = createExportConfig(TruckDTO.class, 0, 1);
    public static final ExportConfig userExport = createExportConfig(UserDTO.class, 0, 1);
    public static final ExportConfig expenseReportByDriverExport = createExportConfig(ExpensesIncurredDTO.class, 0, 1);
}
