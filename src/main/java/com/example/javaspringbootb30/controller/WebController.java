package com.example.javaspringbootb30.controller;

import com.example.javaspringbootb30.model.response.VerifyResponse;
import com.example.javaspringbootb30.service.AuthService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final AuthService authService;
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
    @GetMapping("/xac-thuc-tai-khoan")
    public String getVerifyAccount(@RequestParam String token, Model model){
        VerifyResponse response=authService.verifyAccount(token);
        model.addAttribute("response",response);
        return "verify-account";
    }
}
