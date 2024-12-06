package com.project.logistic_management_2.service.authentication;


import com.project.logistic_management_2.dto.authentication.AuthRequest;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.dto.authentication.AuthResponse;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.user.UserMapper;
import com.project.logistic_management_2.service.JwtService;
import com.project.logistic_management_2.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Autowired
    public AuthServiceImpl(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User register(UserDTO userDto) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userService.findByUsername(userDto.getUsername()) != null) {
            throw new IllegalArgumentException("Username đã được sử dụng.");
        }

        User user = userMapper.toUser(userDto);

        return userService.createUser(userDto);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        try {
            // Xác thuwjc he
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Sai tên đăng nhập hoặc mật khẩu.");
        }

        User user = userService.findByUsername(authRequest.getUsername());
        if (user == null) {
            throw new NotFoundException("Không tìm thấy người dùng.");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}

