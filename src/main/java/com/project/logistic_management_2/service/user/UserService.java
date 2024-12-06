package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User createUser(UserDTO userDto);
    User updateUser(Integer id,UserDTO userDto);
    List<User> getAllUsers(Boolean all);
    User getUserById(Integer id, Boolean all);
    String deleteById(Integer id);
    User findByUsername(String username);
}
