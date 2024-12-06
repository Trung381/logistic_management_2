package com.project.logistic_management_2.mapper.rolePermission;

import com.project.logistic_management_2.dto.rolepermission.PermissionDetail;
import com.project.logistic_management_2.dto.rolepermission.RolePermissionResponse;
import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.entity.RolePermission;
import org.springframework.stereotype.Component;

@Component
public class RolePermissionMapper {

    public void updateRolePermissionFromDto(UpdateRolePermissionRequest request, RolePermission entity) {
        entity.setRoleId(request.getRoleId());
        entity.setPermissionId(request.getPermissionId());
        entity.setCanWrite(request.getCanWrite());
        entity.setCanView(request.getCanView());
        entity.setCanApprove(request.getCanApprove());
        entity.setCanDelete(request.getCanDelete());
        // updatedAt is handled via JPA Auditing
    }

    public RolePermissionResponse toResponse(RolePermission entity) {
        RolePermissionResponse response = new RolePermissionResponse();
        response.setRoleId(entity.getRoleId());
        response.setPermissionId(entity.getPermissionId());
        response.setCanWrite(entity.getCanWrite());
        response.setCanView(entity.getCanView());
        response.setCanApprove(entity.getCanApprove());
        response.setCanDelete(entity.getCanDelete());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public PermissionDetail toPermissionDetail(RolePermissionResponse response) {
        return new PermissionDetail(
                response.getPermissionId(),
                response.getPermissionName(),
                response.getCanWrite(),
                response.getCanView(),
                response.getCanApprove(),
                response.getCanDelete()
        );
    }
}
