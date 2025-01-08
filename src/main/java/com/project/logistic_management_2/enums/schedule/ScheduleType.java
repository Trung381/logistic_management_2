package com.project.logistic_management_2.enums.schedule;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ScheduleType {
    INTERNAL(0, "Chạy nội bộ"),
    PAYROLL(1, "Chạy tính lương");

    @Getter
    private final Integer value;
    private final String note;

//    @JsonValue
    public String getNote() {
        return note;
    }

    ScheduleType(int value, String note) {
        this.value = value;
        this.note = note;
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
