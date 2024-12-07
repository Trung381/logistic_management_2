package com.project.logistic_management_2.service.RolePermisson;

import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.entity.QRolePermission;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.mapper.rolePermission.RolePermissionMapper;
import com.project.logistic_management_2.repository.rolePermission.RolePermissionRepo;
import com.project.logistic_management_2.service.BaseService;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class RolePermissionServiceImpl extends BaseService implements RolePermissionService {

    @Autowired
    private RolePermissionRepo rolePermissionRepo;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public boolean hasPermission(String permissionName, PermissionKey key){
        return checkPermission(permissionName, key);
    }



    @Override
    @Transactional
    public long updateRolePermission(UpdateRolePermissionRequest dto) {

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