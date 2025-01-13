package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.user.UpdateUserDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.enums.permission.PermissionKey;
import com.project.logistic_management_2.enums.permission.PermissionType;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.user.UserMapper;
import com.project.logistic_management_2.repository.user.UserRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.ExportConfig;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private final EntityManager entityManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final PermissionType type = PermissionType.USERS;

    @Override
    public User createUser(UserDTO userDto) {
//        checkPermission(type, PermissionKey.WRITE); //Tạm thời tắt để đăng ký tk test api
        User user = userMapper.toUser(userDto);
        return userRepo.save(user);
    }

    @Override
    public User updateUser(String id, UserDTO updateUserDTO) {
        checkPermission(type, PermissionKey.WRITE);

        User existingUser = userRepo.getUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("User not found with ID: " + id);
        }


        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        entityManager.clear();
        if (!userRepo.updateUser(existingUser, id, updateUserDTO)) {
            throw new RuntimeException("Failed to update user with ID: " + id);
        }

        return userRepo.getUserById(id);
    }


    @Override
    public List<User> getAllUsers(int page) {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.getAll(page);
    }

    @Override
    public User getUserById(String id) {
        checkPermission(type, PermissionKey.VIEW);
        User user = userRepo.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        return user;
    }

    @Override
    public String deleteById(String id) {
        checkPermission(type, PermissionKey.DELETE);
//        repository.deleteUser(id);
        return (userRepo.deleteUser(id) > 0 ? "Deleted" : "failure");
    }

    @Override
    public User findByUsername(String username) {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<UserDTO> getDriver(int page) {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.getDriver(page);
    }

    @Override
    public List<UserDTO> getAdmin(int page) {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.getAdmin(page);
    }

    @Override
    public List<User> importUserData(MultipartFile importFile) {

        checkPermission(type, PermissionKey.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<UserDTO> userDTOList = ExcelUtils.getImportData(workbook, ImportConfig.userImport);

        List<User> users = userMapper.toUserList(userDTOList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return userRepo.saveAll(users);
    }

    @Override
    public ExportExcelResponse exportDriver(int page) throws Exception {
        List<UserDTO> users = userRepo.getDriver(page);

        if (CollectionUtils.isEmpty(users)) {
            throw new Exception("No data");
        }
        String fileName = "Driver Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(users, fileName, ExportConfig.userExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    @Override
    public ExportExcelResponse exportAdmin(int page) throws Exception {
        List<UserDTO> users = userRepo.getAdmin(page);

        if (!CollectionUtils.isEmpty(users)) {
            throw new Exception("No data");
        }
        String fileName = "Admin Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(users, fileName, ExportConfig.userExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
