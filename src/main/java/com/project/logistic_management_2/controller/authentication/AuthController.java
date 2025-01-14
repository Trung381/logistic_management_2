package com.project.logistic_management_2.controller.authentication;

import com.project.logistic_management_2.dto.authentication.AuthRequest;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.dto.authentication.AuthResponse;
import com.project.logistic_management_2.entity.user.User;
import com.project.logistic_management_2.service.authentication.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller xử lý các endpoint liên quan đến xác thực.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint đăng ký người dùng mới.
     *
     * @param userDto Dữ liệu người dùng.
     * @return Thông tin người dùng đã được tạo.
     */
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<User>> registerUser(@Valid @RequestBody UserDTO userDto) {
        User createdUser = authService.register(userDto);
        return ResponseEntity.ok(BaseResponse.ok(createdUser));
    }

    /**
     * Endpoint đăng nhập người dùng.
     *
     * @param authRequest Thông tin đăng nhập.
     * @return JWT token nếu đăng nhập thành công.
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> loginUser(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(BaseResponse.ok(authResponse));
    }
}
