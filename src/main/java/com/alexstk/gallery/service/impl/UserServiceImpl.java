package com.alexstk.gallery.service.impl;

import com.alexstk.gallery.dto.UserDto;
import com.alexstk.gallery.entity.User;
import com.alexstk.gallery.repository.UserRepository;
import com.alexstk.gallery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDto getUser(int id) {
        User user = repository.findById(id);
        UserDto userDto = new UserDto(user.getId(), user.getLogin(), user.getPassword(), String.valueOf(user.getRoleId()));
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = repository.list().stream().map(user -> new UserDto(user.getId(), user.getLogin(), user.getPassword(), String.valueOf(user.getRoleId()))).collect(
                Collectors.toList());
        return userDtos;
    }

    @Override
    public void saveUser(UserDto userDto) {

        User user = new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), Integer.valueOf(userDto.getRole()));
        repository.persist(user);
    }

    @Override
    public void deleteUser(int id) {
        repository.remove(id);
    }

    @Override
    public void createTable(String password){
        if("user".equals(password)){
            repository.createTable();
        }
    }

    @Override
    public void deleteTable(String password){
        if("user".equals(password)){
            repository.deleteTable();
        }
    }
}
