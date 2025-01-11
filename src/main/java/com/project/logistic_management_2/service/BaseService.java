package com.project.logistic_management_2.service;

import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.define.ForbiddenException;
import com.project.logistic_management_2.repository.rolePermission.RolePermissionRepo;
import com.project.logistic_management_2.repository.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class BaseService {
    @Autowired
    UserRepo userRepository;
    @Autowired
    RolePermissionRepo rolePermissonRepo;

    protected User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new ForbiddenException("Login, pleaseeee !!!");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    protected boolean checkPermission(PermissionType type, PermissionKey key) {
        User user = getCurrentUser();
        if (!rolePermissonRepo.hasPermission(user.getRoleId(), type, key)) {
            throw new ForbiddenException("Huh, no permission to access :)");
        }
        return true;
    }
}
