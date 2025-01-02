package com.project.logistic_management_2.dto.user;

import lombok.*;

import java.util.Date;

@Getter
@Setter
public class UpdateUserDTO {
    private String fullName;
    private String phone;
    private Date dateOfBirth;
    private String note;
    private String username;
    private String password;
    private Integer roleId;
    private Integer status;
}
