package com.project.logistic_management_2.repository.rolepermission;

import com.project.logistic_management_2.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolePermissionRepo extends JpaRepository<RolePermission, Integer>, RolePermissionRepoCustom {
    Optional<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}
