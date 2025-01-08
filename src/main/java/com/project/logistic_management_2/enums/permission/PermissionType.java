package com.project.logistic_management_2.enums.permission;

import lombok.Getter;

@Getter
public enum PermissionType {
    USERS("Quản lý người dùng", "USERS"),
    PERMISSIONS("Phân quyền", "PERMISSIONS"),
    CONFIGS("Quản lý cấu hình", "CONFIGS"),
    TRUCKS("Quản lý xe tải", "TRUCKS"),
    EXPENSES("Quản lý chi phí", "EXPENSES"),
    SCHEDULES("Quản lý lịch trình", "SCHEDULES"),
    SALARIES("Quản lý lương", "SALARIES"),
    TRANSACTIONS("Quản lý giao dịch", "TRANSACTIONS"),
    REPORTS("Báo cáo", "REPORTS"),
    WAREHOUSES("Quản lý kho", "WAREHOUSES"),
    GOODS("Quản lý hàng hóa", "GOODS"),
    TRANSACTION("Quản lý giao dịch", "TRANSACTION");
    ;

    private final String title;
    private final String name;

    PermissionType(String title, String name){
        this.title = title;
        this.name = name;
    }
}
