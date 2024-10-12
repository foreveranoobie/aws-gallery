package com.alexstk.gallery.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Value("${spring.profiles.active}")
    private String profileName;

    @GetMapping("/health")
    public String getHealth(){
        return String.format("Hello from EC2 on %s's profile", profileName);
    }
}
