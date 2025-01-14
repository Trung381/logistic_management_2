package com.project.logistic_management_2.enums.attached;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AttachedType {
    ATTACHED_OF_SCHEDULE(0, "Ảnh đính kèm của lịch trình"),
    ATTACHED_OF_EXPENSES(1, "Ảnh đính kèm của chi phí");

    private final Integer value;
    @JsonValue
    private final String description;

    AttachedType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static AttachedType valueOf(int value) {
        AttachedType[] values = AttachedType.values();
        for (AttachedType attachedType : values) {
            if (attachedType.value.equals(value)) {
                return attachedType;
            }
        }
        throw new IllegalArgumentException("");
    }
}
