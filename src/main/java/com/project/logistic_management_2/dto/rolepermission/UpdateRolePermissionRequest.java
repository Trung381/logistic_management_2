package com.project.logistic_management_2.dto.rolepermission;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRolePermissionRequest {
    @NotNull(message = "Role ID cannot be null")
    private Integer roleId;

    @NotNull(message = "Permission ID cannot be null")
    private Integer permissionId;

    @NotNull(message = "Can Write flag cannot be null")
    private Boolean canWrite;

    @NotNull(message = "Can View flag cannot be null")
    private Boolean canView;

    @NotNull(message = "Can Approve flag cannot be null")
    private Boolean canApprove;

    @NotNull(message = "Can Delete flag cannot be null")
    private Boolean canDelete;
}