package com.project.logistic_management_2.enums;

import lombok.Getter;

@Getter
public enum PermissionType {
    VIEW("can_view"),
    WRITE("can_write"),
    DELETE("can_delete"),
    APPROVE("can_approve");

    private final String column;

    PermissionType(String column){
        this.column = column;
    }
}
