package com.example.javaspringbootb30.security;

import com.example.javaspringbootb30.entity.User;
import com.example.javaspringbootb30.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email)
                        .orElseThrow(()->new UsernameNotFoundException("User not found"+email));
        return new CustomUserDetails(user);
    }
}