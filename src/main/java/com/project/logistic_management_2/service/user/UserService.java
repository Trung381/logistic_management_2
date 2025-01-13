package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.ExportExcelResponse;
import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    User createUser(UserDTO userDto);
    User updateUser(String id, UserDTO updateUserDTO);
    List<User> getAllUsers(int page);
    User getUserById(String id);
    String deleteById(String id);
    List<UserDTO> getDriver(int page);
    List<UserDTO> getAdmin(int page);
    List<User> importUserData(MultipartFile importFile);
    ExportExcelResponse exportDriver(int page) throws Exception;
    ExportExcelResponse exportAdmin(int page) throws Exception;
}
