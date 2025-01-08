package com.project.logistic_management_2.enums.truck;

import lombok.Getter;

@Getter
public enum TruckType {
    TRUCK_HEAD(0, "Đầu xe tải"),
    MOOC(1, "Rơ-mooc");

    private final Integer value;
    private final String description;

    TruckType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TruckType valueOf(int value) {
        TruckType[] values = TruckType.values();
        for (TruckType type : values) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("");
    }
}
