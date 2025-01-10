package com.project.logistic_management_2.enums.schedule;

import lombok.Getter;

@Getter
public enum ScheduleStatus {
    REJECTED(-1, "Không duyệt/Bị từ chối"),
    PENDING(0, "Đang chờ duyệt"),
    APPROVED(1, "Đã duyệt"),
    COMPLETED(2, "Đã hoàn thành");

    private final Integer value;
    private final String description;

    ScheduleStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ScheduleStatus valueOf(int value) {
        ScheduleStatus[] values = ScheduleStatus.values();
        for (ScheduleStatus status : values) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
