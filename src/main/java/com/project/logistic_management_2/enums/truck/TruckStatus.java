package com.project.logistic_management_2.enums.truck;

import lombok.Getter;

@Getter
public enum TruckStatus {
    MAINTENANCE(-1, "Đang bảo trì"),
    APPROVED_SCHEDULE(0, "Lịch trình liên quan đã được duyệt"),
    AVAILABLE(1, "Có sẵn"),
    PENDING_SCHEDULE(2, "Lịch trình liên quan đang chờ xử lý");

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
