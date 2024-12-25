package com.project.logistic_management_2.mapper.user;

import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.enums.IDKey;
import com.project.logistic_management_2.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
                .id(Utils.genID(IDKey.USER))
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

    public List<User> toUserList(List<UserDTO> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream().map(dto ->
                User.builder()
                        .id(Utils.genID(IDKey.USER))
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
                        .build()
        ).collect(Collectors.toList());
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
