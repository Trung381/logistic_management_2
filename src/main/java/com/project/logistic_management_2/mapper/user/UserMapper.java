package com.project.logistic_management_2.mapper.user;

import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMapper{

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {

        this.passwordEncoder = passwordEncoder;
    }

    public User toUser(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .note(dto.getNote())
                .dateOfBirth(dto.getDateOfBirth())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roleId(dto.getRoleId())
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public void updateUserMapper(User user, UserDTO dto) {
        if (user == null) {
            return;
        }
        user.setNote(dto.getNote());
        user.setPhone(dto.getPhone());
        user.setFullName(dto.getFullName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoleId(dto.getRoleId());
        user.setStatus(dto.getStatus());
        user.setUpdatedAt(new Date());
    }
}
