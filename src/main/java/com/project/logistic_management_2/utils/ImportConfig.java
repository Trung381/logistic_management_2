package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.dto.expenses.ExpensesConfigDTO;
import com.project.logistic_management_2.dto.expenses.ExpensesDTO;
import com.project.logistic_management_2.dto.request.TransactionDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleConfigDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleDTO;
import com.project.logistic_management_2.dto.truck.TruckDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
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

        transactionImportCellConfigs.add(new CellConfig(0, "Mã người phụ trách","refUserId"));
        transactionImportCellConfigs.add(new CellConfig(1, "Mã hàng","goodsId"));
        transactionImportCellConfigs.add(new CellConfig(2, "Số lượng","quantity"));
        transactionImportCellConfigs.add(new CellConfig(3, "Ghi chú","destination"));
        transactionImportCellConfigs.add(new CellConfig(4, "Tên khách hàng","customerName"));
        transactionImportCellConfigs.add(new CellConfig(5, "Thời gian giao dịch","transactionTime"));
        transactionImportCellConfigs.add(new CellConfig(6, "Loại giao dịch","originDescription"));
        transactionImportCellConfigs.add(new CellConfig(7, "Ảnh hóa đơn","image"));

        transactionImport.setCellImportConfigs(transactionImportCellConfigs);
    }

    public static final ImportConfig expensesImport;
    static{
        expensesImport = new ImportConfig();
        expensesImport.setSheetIndex(0);
        expensesImport.setHeaderIndex(0);
        expensesImport.setStartRow(1);
        expensesImport.setDataClazz(ExpensesDTO.class);
        List<CellConfig> expensesImportCellConfigs = new ArrayList<>();

        expensesImportCellConfigs.add(new CellConfig(0, "Mã tài xế","driverId"));
        expensesImportCellConfigs.add(new CellConfig(1, "Mã loại chi phí","expensesConfigId"));
        expensesImportCellConfigs.add(new CellConfig(2, "Số tiền","amount"));
        expensesImportCellConfigs.add(new CellConfig(3, "Ghi chú","note"));
        expensesImportCellConfigs.add(new CellConfig(4, "Ảnh hóa đơn","imgPath"));
        expensesImportCellConfigs.add(new CellConfig(5, "Mã lịch trình","scheduleId"));

        expensesImport.setCellImportConfigs(expensesImportCellConfigs);
    }

    public static final ImportConfig expensesConfigImport;
    static{
        expensesConfigImport = new ImportConfig();
        expensesConfigImport.setSheetIndex(0);
        expensesConfigImport.setHeaderIndex(0);
        expensesConfigImport.setStartRow(1);
        expensesConfigImport.setDataClazz(ExpensesConfigDTO.class);
        List<CellConfig> expensesConfigImportCellConfigs = new ArrayList<>();

        expensesConfigImportCellConfigs.add(new CellConfig(0, "Loại chi phí","type"));
        expensesConfigImportCellConfigs.add(new CellConfig(1, "Ghi chú","note"));

        expensesConfigImport.setCellImportConfigs(expensesConfigImportCellConfigs);
    }

    public static final ImportConfig scheduleConfigImport;
    static{
        scheduleConfigImport = new ImportConfig();
        scheduleConfigImport.setSheetIndex(0);
        scheduleConfigImport.setHeaderIndex(0);
        scheduleConfigImport.setStartRow(1);
        scheduleConfigImport.setDataClazz(ScheduleConfigDTO.class);
        List<CellConfig> scheduleConfigImportCellConfigs = new ArrayList<>();

        scheduleConfigImportCellConfigs.add(new CellConfig(0, "Điểm đi","placeA"));
        scheduleConfigImportCellConfigs.add(new CellConfig(1, "Điểm đến","placeB"));
        scheduleConfigImportCellConfigs.add(new CellConfig(2, "Giá chuyến","amount"));
        scheduleConfigImportCellConfigs.add(new CellConfig(3, "Ghi chú","note"));

        scheduleConfigImport.setCellImportConfigs(scheduleConfigImportCellConfigs);
    }

    public static final ImportConfig scheduleImport;
    static{
        scheduleImport = new ImportConfig();
        scheduleImport.setSheetIndex(0);
        scheduleImport.setHeaderIndex(0);
        scheduleImport.setStartRow(1);
        scheduleImport.setDataClazz(ScheduleDTO.class);
        List<CellConfig> scheduleImportCellConfigs = new ArrayList<>();

        scheduleImportCellConfigs.add(new CellConfig(0, "Mã hành trình","scheduleConfigId"));
        scheduleImportCellConfigs.add(new CellConfig(1, "Biển số xe","truckLicense"));
        scheduleImportCellConfigs.add(new CellConfig(2, "Biển số mooc","moocLicense"));
        scheduleImportCellConfigs.add(new CellConfig(3, "Ảnh","attachDocument"));
        scheduleImportCellConfigs.add(new CellConfig(4, "Thời gian lấy hàng","departureTime"));
        scheduleImportCellConfigs.add(new CellConfig(5, "Thời gian giao hàng","arrivalTime"));
        scheduleImportCellConfigs.add(new CellConfig(6, "Ghi chú","note"));
        scheduleImportCellConfigs.add(new CellConfig(7, "Loại hành trình","type"));

        scheduleImport.setCellImportConfigs(scheduleImportCellConfigs);
    }

    public static final ImportConfig truckImport;
    static{
        truckImport = new ImportConfig();
        truckImport.setSheetIndex(0);
        truckImport.setHeaderIndex(0);
        truckImport.setStartRow(1);
        truckImport.setDataClazz(TruckDTO.class);
        List<CellConfig> truckImportCellConfigs = new ArrayList<>();

        truckImportCellConfigs.add(new CellConfig(0, "Biển số","licensePlate"));
        truckImportCellConfigs.add(new CellConfig(1, "Dung tích","capacity"));
        truckImportCellConfigs.add(new CellConfig(2, "Mã tài xế","driverId"));
        truckImportCellConfigs.add(new CellConfig(3, "Loại xe","type"));
        truckImportCellConfigs.add(new CellConfig(4, "Ghi chú","note"));

        truckImport.setCellImportConfigs(truckImportCellConfigs);
    }

    public static final ImportConfig userImport;
    static{
        userImport = new ImportConfig();
        userImport.setSheetIndex(0);
        userImport.setHeaderIndex(0);
        userImport.setStartRow(1);
        userImport.setDataClazz(UserDTO.class);
        List<CellConfig> userImportCellConfigs = new ArrayList<>();

        userImportCellConfigs.add(new CellConfig(0, "Họ tên","fullName"));
        userImportCellConfigs.add(new CellConfig(1, "Số ĐT","phone"));
        userImportCellConfigs.add(new CellConfig(2, "Ghi chú","note"));
        userImportCellConfigs.add(new CellConfig(3, "Ngày sinh","dateOfBirth"));
        userImportCellConfigs.add(new CellConfig(4, "Tên tài khoản","username"));
        userImportCellConfigs.add(new CellConfig(5, "Mật khẩu","password"));
        userImportCellConfigs.add(new CellConfig(6, "Mã quyền","roleId"));

        userImport.setCellImportConfigs(userImportCellConfigs);
    }

}
