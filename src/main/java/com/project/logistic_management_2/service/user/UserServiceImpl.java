package com.project.logistic_management_2.service.user;


import com.project.logistic_management_2.dto.user.UserDTO;
import com.project.logistic_management_2.entity.User;
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

    @Override
    public User createUser(UserDTO userDto) {
        User user = userMapper.toUser(userDto);
        return userRepo.save(user);
    }

    @Override
    public User updateUser(Integer id, UserDTO userDto) {
        if (userDto.getId() == null) {
            throw new IllegalArgumentException("User ID must be provided for updating.");
        }
        User user = userRepo.getUserById(id, false);
        if(user == null){
            throw new NotFoundException("User not found with User: "+id);
        }
        userMapper.updateUserMapper(user,userDto);
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers(Boolean all) {
        return userRepo.getAll(all);
    }

    @Override
    public User getUserById(Integer id, Boolean all) {
        User user = userRepo.getUserById(id, all);
        if(user == null){
            throw new NotFoundException("User not found with ID: "+id);
        }
        return user;
    }

    @Override
    public String deleteById(Integer id) {
//        repository.deleteUser(id);
        return (userRepo.deleteUser(id) > 0 ? "Deleted" : "failure");
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}
