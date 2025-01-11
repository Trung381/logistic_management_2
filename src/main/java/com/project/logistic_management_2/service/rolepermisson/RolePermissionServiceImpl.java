package com.project.logistic_management_2.service.rolepermisson;

import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.entity.QRolePermission;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.repository.rolepermission.RolePermissionRepo;
import com.project.logistic_management_2.service.BaseService;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends BaseService implements RolePermissionService {

    private final RolePermissionRepo rolePermissionRepo;
    private final PermissionType type = PermissionType.PERMISSIONS;

    @Override
    public boolean hasPermission(PermissionType type, PermissionKey key){
        return checkPermission(type, key);
    }

    @Override
    @Transactional
    public long updateRolePermission(UpdateRolePermissionRequest dto) {

        checkPermission(type, PermissionKey.WRITE);

        List<Path<?>> paths = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        QRolePermission qRolePermission = QRolePermission.rolePermission;

        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                PathBuilder<Object> path = new PathBuilder<>(Object.class, qRolePermission.getMetadata().getName() + "." + field.getName());
                paths.add(path);
                Object value = field.get(dto);
                if (value != null) {
                    values.add(value);
                }
            } catch (Exception _) {
            }
        }

        return rolePermissionRepo.changePermissionByRoleId(dto.getRoleId(), dto.getPermissionId(), paths, values);
    }


}
