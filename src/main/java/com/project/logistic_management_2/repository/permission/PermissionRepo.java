package com.project.logistic_management_2.repository.permission;


import com.project.logistic_management_2.entity.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Integer>, PermissionRepoCustom {
}
