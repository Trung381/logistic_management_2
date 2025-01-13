package com.project.logistic_management_2.enums.truck;

import lombok.Getter;

@Getter
public enum TruckStatus {
    MAINTENANCE(-1, "Đang bảo trì"),
    ON_JOURNEY(0, "Đang trong hành trình"),
    AVAILABLE(1, "Có sẵn"),
    WAITING_FOR_SCHEDULE_APPROVAL(2, "Chờ lịch trình được duyệt");

    private final Integer value;
    private final String description;

    TruckStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TruckStatus valueOf(int value) {
        TruckStatus[] values = TruckStatus.values();
        for (TruckStatus status : values) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
