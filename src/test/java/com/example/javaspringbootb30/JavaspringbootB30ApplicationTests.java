package com.example.javaspringbootb30;

import com.example.javaspringbootb30.entity.Role;
import com.example.javaspringbootb30.entity.User;
import com.example.javaspringbootb30.repository.RoleRepository;
import com.example.javaspringbootb30.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class JavaspringbootB30ApplicationTests {
@Autowired
private RoleRepository roleRepository;
@Autowired
private UserRepository userRepository;
@Autowired
private PasswordEncoder passwordEncoder;
    @Test
    void save_roles() {
        Role userRole = Role.builder().name("USER").build();
        Role adminRole = Role.builder().name("ADMIN").build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
    }

    @Test
    void save_users(){
        Role userRole=roleRepository.findByName("USER").orElse(null);
        Role adminRole=roleRepository.findByName("ADMIN").orElse(null);

        User user1= User.builder()
                .name("khue")
                .email("khue@gmail.com")
                .password(passwordEncoder.encode("123"))
                .roles(List.of(userRole,adminRole))
                .build();
        userRepository.save(user1);

        User user2= User.builder()
                .name("khue")
                .email("khue1@gmail.com")
                .password(passwordEncoder.encode("123"))
                .roles(List.of(userRole))
                .build();
        userRepository.save(user2);
    }

}
