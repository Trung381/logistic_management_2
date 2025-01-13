package com.project.logistic_management_2.controller.authorization;


import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.service.rolepermisson.RolePermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role-permission")
@RequiredArgsConstructor
public class AuthorizationController {
    private final RolePermissionService rolePermissionService;

    @PostMapping("/check-permission")
    public ResponseEntity<BaseResponse<Boolean>> checkPermisson(@RequestParam PermissionType permissionName, @RequestParam PermissionKey key){
        return ResponseEntity.ok(BaseResponse.ok(rolePermissionService.hasPermission(permissionName, key)));
    }

    @PostMapping("/authorization")
    public ResponseEntity<BaseResponse<Boolean>> updateRolePermission(@Valid @RequestBody UpdateRolePermissionRequest request) {

        long updatedRolePermission = rolePermissionService.updateRolePermission(request);
        return ResponseEntity.ok(BaseResponse.ok(updatedRolePermission >0));
    }
}
