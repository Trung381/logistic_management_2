package com.project.logistic_management_2.repository.role;

import com.project.logistic_management_2.entity.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>, RoleRepoCustom {
}
