package com.alexstk.gallery.controller;

import com.alexstk.gallery.api.user.UserResponse;
import com.alexstk.gallery.dto.UserDto;
import com.alexstk.gallery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping(path = "/{id}")
    public UserResponse getById(@PathVariable int id) {
        UserDto userDto = service.getUser(id);
        return new UserResponse(userDto.getId(), userDto.getLogin(), userDto.getPassword(), userDto.getRole());
    }

    @GetMapping
    public List<UserResponse> getAll() {
        List<UserResponse> users = service.getAllUsers()
                .stream()
                .map(userDto -> new UserResponse(userDto.getId(), userDto.getLogin(), userDto.getPassword(),
                        userDto.getRole()))
                .collect(Collectors.toList());
        return users;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody UserDto createDto) {
        service.saveUser(createDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        service.deleteUser(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/table/{password}")
    public ResponseEntity deleteTable(@PathVariable String password){
        service.deleteTable(password);
        return ResponseEntity.status(410).build();
    }

    @PutMapping("/table/{password}")
    public ResponseEntity createTable(@PathVariable String password){
        service.createTable(password);
        return ResponseEntity.status(201).build();
    }
}
