package com.project.logistic_management_2.dto.rolepermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDetail {
    private Integer permissionId;
    private String permissionName;
    private Boolean canWrite;
    private Boolean canView;
    private Boolean canApprove;
    private Boolean canDelete;
}
