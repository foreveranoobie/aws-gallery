package com.alexstk.gallery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/health")
    public String getHealth(){
        return "Hello from EC2";
    }
}
