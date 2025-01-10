package com.project.logistic_management_2.enums.transaction;

import lombok.Getter;

@Getter
public enum TransactionType {
    INBOUND_TRANSACTION(false, "Nhập hàng"),
    OUTBOUND_TRANSACTION(true, "Xuất hàng");

    private final Boolean value;
    private final String title;

    TransactionType(Boolean value, String title) {
        this.value = value;
        this.title = title;
    }

    public static TransactionType valueOf(Boolean value) {
        if(value) return OUTBOUND_TRANSACTION;
        else return INBOUND_TRANSACTION;
    }
}
