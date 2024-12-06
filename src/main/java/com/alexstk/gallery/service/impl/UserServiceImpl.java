package com.alexstk.gallery.service.impl;

import com.alexstk.gallery.dto.UserDto;
import com.alexstk.gallery.dto.UserGroupDto;
import com.alexstk.gallery.entity.User;
import com.alexstk.gallery.repository.UserRepository;
import com.alexstk.gallery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user ->
                        new UserDto(user.getId(), user.getName(), user.getPassword(), UserGroupDto.valueOf(user.getRole().getRoleType().name())))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDto(user.getId(), user.getName(), user.getPassword(), UserGroupDto.valueOf(user.getRole().getRoleType().name()));
        }
        return null;
    }

    @Override
    public UserDto getUserByName(String name) {
        Optional<User> userOptional = userRepository.findOneByName(name);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDto(user.getId(), user.getName(), user.getPassword(), UserGroupDto.valueOf(user.getRole().getRoleType().name()));
        }
        return null;
    }
}
