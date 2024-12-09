package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(UserDTO userDto);
    User updateUser(String id,UserDTO userDto);
    List<User> getAllUsers();
    User getUserById(String id);
    String deleteById(String id);
    User findByUsername(String username);
}
