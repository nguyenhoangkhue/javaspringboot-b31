package com.example.javaspringbootb30.service;

import com.example.javaspringbootb30.entity.Role;
import com.example.javaspringbootb30.entity.User;
import com.example.javaspringbootb30.exception.BadRequestException;
import com.example.javaspringbootb30.model.request.LoginRequest;
import com.example.javaspringbootb30.model.request.SignupRequest;
import com.example.javaspringbootb30.repository.RoleRepository;
import com.example.javaspringbootb30.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticattionManger;
    private final HttpSession session;
    public void login(LoginRequest request){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        try{
            //tien hanh xac thuc
            Authentication authentication = authenticattionManger.authenticate(token);
            //luu doi tuong da xac thuc vao trong securitycontextholder
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //luu vao trong session
            session.setAttribute("MY_SESSION",authentication.getName());
        }catch (AuthenticationException e){
            throw new BadRequestException("Tai khoan hoac mat khau khong dung");
        }
    }
    public void signup(SignupRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new BadRequestException("Email already exist");
        }
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new BadRequestException("Role does not exist"));
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .build();
        userRepository.save(newUser);
    }
}
