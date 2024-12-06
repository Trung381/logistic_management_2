package com.project.logistic_management_2.enums;

import lombok.AllArgsConstructor;

public enum IDKey {
    EXPENSES("EX"),
    EXPENSES_CONFIG("EXC"),
    SCHEDULE("SCD"),
    SCHEDULE_CONFIG("SCDC"),
    USER("US"),
    TRUCK("TRUCK"),
    WAREHOUSE("WH"),
    GOODS("GS"),
    TRANSACTION("TRANS");

    public final String label;

    private IDKey(String label) {
        this.label = label;
    }
}
