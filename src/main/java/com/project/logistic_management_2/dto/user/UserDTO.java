package com.project.logistic_management_2.dto.user;

import com.project.logistic_management_2.annotations.ExportColumn;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @ExportColumn(name = "Mã NV")
    private String id;

    @NotBlank(message = "Tên không được để trống")
    @ExportColumn(name = "Họ tên")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0(?!0)\\d{9}$", message = "Số điện thoại là dãy 10 chữ số và bắt đầu bằng 0")
    @ExportColumn(name = "Số ĐT")
    private String phone;

    @NotNull(message = "Ngày sinh không được để trống")
    @ExportColumn(name = "Ngày sinh")
    private Date dateOfBirth;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự.")
    private String password;

    @NotNull(message = "Role ID không được để trống.")
    private Integer roleId;
    @ExportColumn(name = "Loại tài khoản")
    private String roleName;

    private Integer status;
    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
}

