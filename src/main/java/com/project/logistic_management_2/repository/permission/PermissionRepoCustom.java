package com.project.logistic_management_2.repository.permission;



import com.project.logistic_management_2.entity.Permission;

import java.util.List;

public interface PermissionRepoCustom {
    public List<Permission> getPermissionsByRoleId(Integer roleId);
}
