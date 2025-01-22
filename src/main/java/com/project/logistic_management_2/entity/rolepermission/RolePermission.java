package com.project.logistic_management_2.entity.rolepermission;

import com.project.logistic_management_2.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_permission")
public class RolePermission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "can_write")
    private Boolean canWrite;

    @Column(name = "can_view")
    private Boolean canView;

    @Column(name = "can_approve")
    private Boolean canApprove;

    @Column(name = "can_delete")
    private Boolean canDelete;
}
