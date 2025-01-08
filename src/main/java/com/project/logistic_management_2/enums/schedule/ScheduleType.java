package com.project.logistic_management_2.enums.schedule;

import lombok.Getter;

public enum ScheduleType {
    INTERNAL(0, "Chạy nội bộ"),
    PAYROLL(1, "Chạy tính lương");

    @Getter
    private final Integer value;
    private final String description;

//    @JsonValue
    public String getNote() {
        return description;
    }

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
