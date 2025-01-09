package com.project.logistic_management_2.enums.transaction;

import lombok.Getter;

@Getter
public enum TransactionType {
    INBOUND_TRANSACTION(0, "Giao dịch nhập"),
    OUTBOUND_TRANSACTION(1, "Giao dịch xuất");

    private final int value;
    private final String title;

    TransactionType(int value, String title) {
        this.value = value;
        this.title = title;
    }

    public static TransactionType valueOf(int value) {
        TransactionType[] values = TransactionType.values();
        for (TransactionType status : values) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
