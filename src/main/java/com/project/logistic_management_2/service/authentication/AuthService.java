package com.project.logistic_management_2.service.authentication;

import com.project.logistic_management_2.dto.authentication.AuthRequest;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.dto.authentication.AuthResponse;
import com.project.logistic_management_2.entity.user.User;

public interface AuthService {
    User register(UserDTO userDto);
    AuthResponse login(AuthRequest authRequest);
}

