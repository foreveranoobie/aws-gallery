package com.alexstk.gallery.controller;

import com.alexstk.gallery.api.user.UserResponse;
import com.alexstk.gallery.dto.UserDto;
import com.alexstk.gallery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(user -> new UserResponse(user.id(), user.name(), user.group().name())).collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<UserResponse> getByName(@PathVariable String name){
        UserDto userDto = userService.getUserByName(name);
        UserResponse userResponse = new UserResponse(userDto.id(), userDto.name(), userDto.group().name());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
