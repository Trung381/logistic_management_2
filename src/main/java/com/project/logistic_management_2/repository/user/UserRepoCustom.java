package com.project.logistic_management_2.repository.user;

import com.project.logistic_management_2.dto.user.UpdateUserDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;

import java.util.List;

public interface UserRepoCustom {
    List<User> getAll(int page);
    User getUserById(String id);
    long deleteUser(String id);
    User findByUsername(String username);
    List<UserDTO> getDriver(int page);
    List<UserDTO> getAdmin(int page);
    Boolean updateUser(User exitingUser,String id, UserDTO updateUserDTO);
}
