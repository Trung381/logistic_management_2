package com.project.logistic_management_2.dto.request;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReportDTO {

//    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    private Long id;

    @NotBlank(message = "ID hàng hóa không được để trống")
    @ExportColumn(name = "Mã hiệu")
    private String goodsId;
    @ExportColumn(name = "Tên VTHH")
    private String goodsName;

    @ExportColumn(name = "Số lượng tồn đầu kỳ")
    private Float beginingInventoryQuantity;
    @ExportColumn(name = "Giá trị tồn đầu kỳ")
    private Float beginingInventoryTotalAmount;

    @ExportColumn(name = "Số lượng nhập trong kỳ")
    private Float inboundTransactionQuantity;
    @ExportColumn(name = "Giá trị nhập trong kỳ")
    private Float inboundTransactionTotalAmount;

    @ExportColumn(name = "Số lượng xuất trong kỳ")
    private Float outboundTransactionQuantity;
    @ExportColumn(name = "Giá trị xuất trong kỳ")
    private Float outboundTransactionTotalAmount;

    @ExportColumn(name = "Số lượng tồn cuối kỳ")
    private Float endingInventoryQuantity;
    @ExportColumn(name = "Giá trị tồn cuối kỳ")
    private Float endingInventoryTotalAmount;

    @ExportColumn(name = "Đơn giá")
    private Float unitAmount;

    private Date createdAt;

//    public String getBeginingInventoryQuantity() {
//        return formatFloat(beginingInventoryQuantity);
//    }
//
//    public String getBeginingInventoryTotalAmount() {
//        return formatFloat(beginingInventoryTotalAmount);
//    }
//
//    public String getInboundTransactionQuantity() {
//        return formatFloat(inboundTransactionQuantity);
//    }
//
//    public String getInboundTransactionTotalAmount() {
//        return formatFloat(inboundTransactionTotalAmount);
//    }
//
//    public String getOutboundTransactionQuantity() {
//        return formatFloat(outboundTransactionQuantity);
//    }
//
//    public String getOutboundTransactionTotalAmount() {
//        return formatFloat(outboundTransactionTotalAmount);
//    }
//
//    public String getEndingInventoryQuantity() {
//        return formatFloat(endingInventoryQuantity);
//    }
//
//    public String getEndingInventoryTotalAmount() {
//        return formatFloat(endingInventoryTotalAmount);
//    }
//
//    public String getUnitAmount() {
//        return formatFloat(unitAmount);
//    }
//
//    // Hàm định dạng Float
//    private String formatFloat(Float value) {
//        if (value == null) {
//            return "";
//        }
//        return DECIMAL_FORMAT.format(value);
//    }
}
