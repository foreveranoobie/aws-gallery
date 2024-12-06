package com.alexstk.gallery.service;

import com.alexstk.gallery.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(int id);

    UserDto getUserByName(String name);
}
