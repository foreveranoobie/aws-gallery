package com.alexstk.gallery.service;

import com.alexstk.gallery.dto.UserDto;
import com.alexstk.gallery.entity.User;

import java.util.List;

public interface UserService {
    UserDto getUser(int id);

    List<UserDto> getAllUsers();

    void saveUser(UserDto userDto);

    void deleteUser(int id);

    void createTable(String password);

    void deleteTable(String password);
}
