package com.project.logistic_management_2.service.RolePermisson;


import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.enums.PermissionKey;

import java.util.List;

public interface RolePermissionService {
    boolean hasPermission(String permissionName, PermissionKey key);
    long updateRolePermission(UpdateRolePermissionRequest request);
}
