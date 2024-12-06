package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.entity.User;

import java.util.List;

public interface UserRepoCustom {
    List<User> getAll(Boolean all);
    User getUserById(String id, Boolean all);
    long deleteUser(String id);
    User findByUsername(String username);
}
