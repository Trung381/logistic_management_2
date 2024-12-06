package com.project.logistic_management_2.repository.role;

import com.project.logistic_management_2.entity.Role;

import java.util.List;


public interface RoleRepoCustom {
    Role findRoleById(Integer id);
    List<Role> getAll();
    void deleteRoleById(Integer id);
}
