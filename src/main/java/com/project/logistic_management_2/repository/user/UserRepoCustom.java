package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.entity.User;

import java.util.List;

public interface UserRepoCustom {
    List<User> getAll();
    User getUserById(String id);
    long deleteUser(String id);
    User findByUsername(String username);
}
