package com.project.logistic_management_2.repository.permission;


import com.project.logistic_management_2.entity.permission.Permission;
import com.project.logistic_management_2.entity.permission.QPermission;
import com.project.logistic_management_2.entity.rolepermission.QRolePermission;
import com.project.logistic_management_2.repository.BaseRepo;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PermissionRepoImpl extends BaseRepo implements PermissionRepoCustom {
    public PermissionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        QPermission permission = QPermission.permission;
        QRolePermission rolePermission = QRolePermission.rolePermission;

        return query.select(permission)
                .from(permission)
                .innerJoin(rolePermission)
                .on(permission.id.eq(rolePermission.permissionId))
                .where(rolePermission.roleId.eq(roleId))
                .fetch();
    }
}
