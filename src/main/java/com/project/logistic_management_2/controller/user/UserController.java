package com.project.logistic_management_2.controller.user;


import com.project.logistic_management_2.dto.user.UpdateUserDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.dto.BaseResponse;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.service.user.UserService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
    public ResponseEntity<BaseResponse<User>> updateUser(@PathVariable String id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        User updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(BaseResponse.ok(updatedUser));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
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
    public ResponseEntity<BaseResponse<List<UserDTO>>> getDriver() {
        List<UserDTO> users = userService.getDriver();
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAdmin() {
        List<UserDTO> users = userService.getAdmin();
        return ResponseEntity.ok(BaseResponse.ok(users));
    }

    @GetMapping("/export/driver")
    public ResponseEntity<Object> exportDriver() throws Exception {
        List<UserDTO> users = userService.getDriver();

        if (!CollectionUtils.isEmpty(users)) {
            String fileName = "Driver Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(users, fileName, ExportConfig.userExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");

        }
    }

    @GetMapping("/export/admin")
    public ResponseEntity<Object> exportAdmin() throws Exception {
        List<UserDTO> users = userService.getAdmin();

        if (!CollectionUtils.isEmpty(users)) {
            String fileName = "Admin Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(users, fileName, ExportConfig.userExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    )
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                    .body(inputStreamResource);
        } else {
            throw new Exception("No data");

        }
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importScheduleData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(userService.importUserData(importFile)),
                HttpStatus.CREATED
        );
    }
}

