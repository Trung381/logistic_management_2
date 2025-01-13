package com.project.logistic_management_2.controller.user;


import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.entity.user.User;
import com.project.logistic_management_2.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<BaseResponse<User>> updateUser(@PathVariable String id, @RequestBody UserDTO updateUserDTO) {
        User updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(BaseResponse.ok(updatedUser));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers(@RequestParam int page) {
        List<User> users = userService.getAllUsers(page);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<User>> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(BaseResponse.ok(user));
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<String>> deleteUserById(@PathVariable String id) {
        String notice = userService.deleteById(id);
        return ResponseEntity.ok(BaseResponse.ok(notice));
    }

    @GetMapping("/driver")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getDriver(@RequestParam int page) {
        List<UserDTO> users = userService.getDriver(page);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAdmin(@RequestParam int page) {
        List<UserDTO> users = userService.getAdmin(page);
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/export/driver")
    public ResponseEntity<Object> exportDriver(@RequestParam int page) throws Exception {

        ExportExcelResponse exportExcelResponse = userService.exportDriver(page);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @GetMapping("/export/admin")
    public ResponseEntity<Object> exportAdmin(@RequestParam int page) throws Exception {

        ExportExcelResponse exportExcelResponse = userService.exportAdmin(page);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFileName(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importScheduleData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(userService.importUserData(importFile)),
                HttpStatus.CREATED
        );
    }
}

