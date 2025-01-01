package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.user.UpdateUserDTO;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.user.UserMapper;
import com.project.logistic_management_2.repository.user.UserRepo;
import com.project.logistic_management_2.service.BaseService;
import com.project.logistic_management_2.utils.ExcelUtils;
import com.project.logistic_management_2.utils.FileFactory;
import com.project.logistic_management_2.utils.ImportConfig;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

//    @Override
//    public User updateUser(String id, UpdateUserDTO updateUserDTO) {
//        checkPermission(type, PermissionKey.WRITE);
//        User user = userRepo.getUserById(id);
//        if (user == null) {
//            throw new NotFoundException("User not found with User: " + id);
//        }
//
//        // Mã hóa mật khẩu nếu được cung cấp
//        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
//            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
//        }
//
//        User userUpdated = null;
//
//        if(userRepo.updateUser(id, updateUserDTO) > 0){
//            entityManager.refresh(user);
//            userUpdated = userRepo.getUserById(id);
//        }
//
//        return userUpdated;
//    }

//    @Override
//    public User updateUser(String id, UpdateUserDTO updateUserDTO) {
//        checkPermission(type, PermissionKey.WRITE);
//
////        // Kiểm tra user tồn tại
////        User existingUser = userRepo.getUserById(id);
////        if (existingUser == null) {
////            throw new NotFoundException("User not found with ID: " + id);
////        }
//
//        // Mã hóa mật khẩu nếu có thay đổi
//        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
//            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
//        }
//
//        User updated = null;
//
//        if(userRepo.updateUser(id, updateUserDTO)){
//            updated = getUserById(id);
//        }
//
//        assert updated != null;
//        System.err.println(updated.getFullName());
//        // Trả về bản ghi đã được cập nhật
//        return updated;
//    }

    @Override
    public User updateUser(String id, UpdateUserDTO updateUserDTO) {
        checkPermission(type, PermissionKey.WRITE);

        // Check if user exists
        User existingUser = userRepo.getUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("User not found with ID: " + id);
        }

        // Encode password if provided
        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            updateUserDTO.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }

        // Perform the update
        boolean updated = userRepo.updateUser(id, updateUserDTO);
        if (!updated) {
            throw new RuntimeException("Failed to update user with ID: " + id);
        }

        // Clear the JPA first-level cache to force a fresh database read
        entityManager.clear();

        // Fetch the updated user
        User updatedUser = userRepo.getUserById(id);
        if (updatedUser == null) {
            throw new NotFoundException("Updated user not found with ID: " + id);
        }

        return updatedUser;
    }



    @Override
    public List<User> getAllUsers() {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.getAll();
    }

    @Override
    public User getUserById(String id) {
        checkPermission(type, PermissionKey.VIEW);
        User user = userRepo.getUserById(id);
        if(user == null){
            throw new NotFoundException("User not found with ID: "+id);
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
    public List<UserDTO> getDriver() {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.getDriver();
    }

    @Override
    public List<UserDTO> getAdmin() {
        checkPermission(type, PermissionKey.VIEW);
        return userRepo.getAdmin();
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
}
