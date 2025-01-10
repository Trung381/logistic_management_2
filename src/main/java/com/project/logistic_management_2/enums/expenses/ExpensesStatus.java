package com.project.logistic_management_2.enums.expenses;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ExpensesStatus {
    PENDING(0, "Chờ xử lý"),
    APPROVED(1, "Đã duyệt");

    private final Integer value;
    private final String description;

    ExpensesStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ExpensesStatus valueOf(int value) {
        ExpensesStatus[] values = ExpensesStatus.values();
        for (ExpensesStatus status : values) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
