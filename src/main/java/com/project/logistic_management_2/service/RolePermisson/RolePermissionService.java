package com.project.logistic_management_2.service.RolePermisson;


import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;

public interface RolePermissionService {
    boolean hasPermission(PermissionType type, PermissionKey key);
    long updateRolePermission(UpdateRolePermissionRequest request);
}
