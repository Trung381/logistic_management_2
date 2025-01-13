package com.project.logistic_management_2.enums.role;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN(1),
    ACCOUNTANT(2),
    MANAGER(3),
    DRIVER(4);

    private final Integer id;

    UserRole(int id) {
        this.id = id;
    }

    public static UserRole valueOf(Integer value) {
        UserRole[] values = UserRole.values();
        for (UserRole roleName : values) {
            if (roleName.id.equals(value)) {
                return roleName;
            }
        }
        throw new IllegalArgumentException("");
    }
}
