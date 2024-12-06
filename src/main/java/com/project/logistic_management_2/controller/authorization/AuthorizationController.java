package com.project.logistic_management_2.controller.authorization;


import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.rolepermission.UpdateRolePermissionRequest;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.service.RolePermisson.RolePermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role-permission")
public class AuthorizationController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping("/check-permission")
    public ResponseEntity<BaseResponse<Boolean>> checkPermisson(@RequestParam String permissionName, @RequestParam PermissionKey key){
        return ResponseEntity.ok(BaseResponse.ok(rolePermissionService.hasPermission(permissionName, key)));
    }

    @PostMapping("/authorization")
    public ResponseEntity<BaseResponse<Boolean>> updateRolePermission(@Valid @RequestBody UpdateRolePermissionRequest request) {

        long updatedRolePermission = rolePermissionService.updateRolePermission(request);
        return ResponseEntity.ok(BaseResponse.ok(updatedRolePermission >0));
    }
}
