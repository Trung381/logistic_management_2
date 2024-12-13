package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
import com.project.logistic_management_2.enums.PermissionKey;
import com.project.logistic_management_2.enums.PermissionType;
import com.project.logistic_management_2.exception.def.NotFoundException;
import com.project.logistic_management_2.mapper.user.UserMapper;
import com.project.logistic_management_2.repository.user.UserRepo;
import com.project.logistic_management_2.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PermissionType type = PermissionType.USERS;

    @Override
    public User createUser(UserDTO userDto) {
//        checkPermission(type, PermissionKey.WRITE); //Tạm thời tắt để đăng ký tk test api
        User user = userMapper.toUser(userDto);
        return userRepo.save(user);
    }

    @Override
    public User updateUser(String id, UserDTO userDto) {
        checkPermission(type, PermissionKey.WRITE);
        if (userDto.getId() == null) {
            throw new IllegalArgumentException("User ID must be provided for updating.");
        }
        User user = userRepo.getUserById(id);
        if(user == null){
            throw new NotFoundException("User not found with User: "+id);
        }
        userMapper.updateUserMapper(user,userDto);
        return userRepo.save(user);
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
}
