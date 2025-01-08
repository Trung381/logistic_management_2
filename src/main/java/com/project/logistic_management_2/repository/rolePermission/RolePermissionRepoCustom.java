package com.project.logistic_management_2.repository.rolePermission;


import com.project.logistic_management_2.dto.rolepermission.RolePermissionResponse;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.querydsl.core.types.Path;

import java.util.List;

public interface RolePermissionRepoCustom {
    List<RolePermissionResponse> fetchRolePermissions();
    boolean hasPermission(Integer roleId, PermissionType type, PermissionKey key);
    long changePermissionByRoleId(Integer roleId, Integer permissionId, List<Path<?>> paths, List<?> values);
}
