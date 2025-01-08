package com.project.logistic_management_2.enums.schedule;

import lombok.Getter;

@Getter
public enum ScheduleStatus {
    NOT_APPROVED(-1, "Không duyệt"),
    WAITING_FOR_APPROVAL(0, "Đang chờ duyệt"),
    APPROVED(1, "Đã duyệt"),
    COMPLETED(2, "Đã hoàn thành");

    private final int value;
    private final String title;

    ScheduleStatus(int value, String title) {
        this.value = value;
        this.title = title;
    }

    public static ScheduleStatus valueOf(int value) {
        ScheduleStatus[] values = ScheduleStatus.values();
        for (ScheduleStatus status : values) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
