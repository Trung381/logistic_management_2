package com.project.logistic_management_2.enums.permission;

import lombok.Getter;

@Getter
public enum PermissionKey {
    VIEW("can_view"),
    WRITE("can_write"),
    DELETE("can_delete"),
    APPROVE("can_approve");

    private final String column;

    PermissionKey(String column){
        this.column = column;
    }
}
