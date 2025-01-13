package com.project.logistic_management_2.enums.schedule;

import lombok.Getter;

@Getter
public enum ScheduleType {
    INTERNAL(0, "Chạy nội bộ"),
    PAYROLL(1, "Chạy tính lương");

    private final Integer value;
    private final String description;

    ScheduleType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ScheduleType valueOf(Integer value) {
        ScheduleType[] values = ScheduleType.values();
        for (ScheduleType scheduleType : values) {
            if (scheduleType.value.equals(value)) {
                return scheduleType;
            }
        }
        throw new IllegalArgumentException("");
    }
}
