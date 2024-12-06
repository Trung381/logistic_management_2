package com.project.logistic_management_2.repository.rolePermission;


import com.project.logistic_management_2.dto.rolepermission.RolePermissionResponse;
import com.project.logistic_management_2.entity.QPermission;
import com.project.logistic_management_2.entity.QRole;
import com.project.logistic_management_2.entity.QRolePermission;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RolePermissionRepoImpl extends BaseRepo implements RolePermissionRepoCustom {
    public RolePermissionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<RolePermissionResponse> fetchRolePermissions() {
        QRole role = QRole.role;
        QRolePermission rolePermission = QRolePermission.rolePermission;
        QPermission permission = QPermission.permission;

        return query
                .select(Projections.constructor(RolePermissionResponse.class,
                        role.id.as("roleId"),
                        role.name.as("roleName"),
                        permission.id.as("permissionId"),
                        permission.name.as("permissionName"),
                        rolePermission.canWrite,
                        rolePermission.canView,
                        rolePermission.canApprove,
                        rolePermission.canDelete,
                        rolePermission.createdAt.as("rolePermissionCreatedAt"),
                        rolePermission.updatedAt.as("rolePermissionUpdatedAt")
                ))
                .from(role)
                .innerJoin(rolePermission).on(role.id.eq(rolePermission.roleId))
                .innerJoin(permission).on(rolePermission.permissionId.eq(permission.id))
                .fetch();
    }

    @Override
    public boolean hasPermission(Integer roleId, String permissionName, PermissionKey key) {
        QRolePermission qRolePermission = QRolePermission.rolePermission;
        QPermission qPermission = QPermission.permission;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qRolePermission.roleId.eq(roleId))
                .and(qPermission.name.eq(permissionName));

        if (key != null){
            switch(key){
                case READ:
                    builder.and(qRolePermission.canView.eq(true));
                    break;
                case CREATE:
                    builder.and(qRolePermission.canWrite.eq(true));
                    break;
                case APPROVE:
                    builder.and(qRolePermission.canApprove.eq(true));
                    break;
                case DELETE:
                    builder.and(qRolePermission.canDelete.eq(true));
                    break;
            }
        }

        Long count = query
                .select(qRolePermission.id.count())
                .from(qRolePermission)
                .innerJoin(qPermission).on(qRolePermission.permissionId.eq(qPermission.id))
                .where(builder)
                .fetchOne();

        return count != null && count > 0;
    }

    @Override
    @Transactional
    public long changePermissionByRoleId(Integer roleId, Integer permissionId, List<Path<?>> paths, List<?> values) {
        QRolePermission qRolePermission = QRolePermission.rolePermission;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qRolePermission.roleId.eq(roleId))
                .and(qRolePermission.permissionId.eq(permissionId));

        return query.update(qRolePermission)
                .where(builder)
                .set(paths, values)
                .set(qRolePermission.updatedAt, new Date())
                .execute();
    }
}
