package com.project.logistic_management_2.controller.user;


import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<User>> createUser(@Valid @RequestBody UserDTO userDto) {
        User createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(BaseResponse.ok(createdUser));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<BaseResponse<User>> updateUser(@PathVariable String id, @Valid @RequestBody UserDTO userDto) {
        User updatedUser = userService.updateUser(id,userDto);
        return ResponseEntity.ok(BaseResponse.ok(updatedUser));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers(@RequestParam Boolean all) {
        List<User> users = userService.getAllUsers(all);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<User>> getUserById(@PathVariable String id, @RequestParam Boolean all) {
        User user = userService.getUserById(id,all);
        return ResponseEntity.ok(BaseResponse.ok(user));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<String>> deleteUserById(@PathVariable String id) {
        String notice = userService.deleteById(id);
        return ResponseEntity.ok(BaseResponse.ok(notice));
    }
}

