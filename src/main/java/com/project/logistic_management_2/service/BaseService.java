package com.project.logistic_management_2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class BaseService<R, M> {
    protected R repository;
    protected M mapper;

    public BaseService(R repo, M mapper) {
        this.repository = repo;
        this.mapper = mapper;
    }

//    @Autowired
//    UserRepo userRepository;
//    @Autowired
//    RolePermissionRepo rolePermissonRepo;
//
//    protected User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() ||
//                authentication.getPrincipal().equals("anonymousUser")) {
//            throw new ForbiddenException("Login, pleaseeee !!!");
//        }
//        String username = authentication.getName();
//        return userRepository.findByUsername(username);
//    }
//
//    protected boolean checkPermission(String permission, PermissionKey key) {
//        User user = getCurrentUser();
//        if (!rolePermissonRepo.hasPermission(user.getRoleId(), permission, key)) {
//            throw new ForbiddenException("Huh, no permission to access :)");
//        }
//        return true;
//    }
}
