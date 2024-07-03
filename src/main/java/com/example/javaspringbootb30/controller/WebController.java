package com.example.javaspringbootb30.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    @GetMapping("/")
    public String getHome(){
        return "index";
    }
    @GetMapping("/users")
    public String getUser() {
        return "user";
    }
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admins")
    public String createUser() {
        return "admin";
    }
    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
