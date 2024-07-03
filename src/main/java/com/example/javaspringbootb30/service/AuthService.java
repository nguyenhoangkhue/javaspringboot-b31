package com.example.javaspringbootb30.service;

import com.example.javaspringbootb30.entity.Role;
import com.example.javaspringbootb30.entity.TokenConfirm;
import com.example.javaspringbootb30.entity.User;
import com.example.javaspringbootb30.exception.BadRequestException;
import com.example.javaspringbootb30.model.enums.TokenType;
import com.example.javaspringbootb30.model.request.LoginRequest;
import com.example.javaspringbootb30.model.request.SignupRequest;
import com.example.javaspringbootb30.model.response.VerifyResponse;
import com.example.javaspringbootb30.repository.RoleRepository;
import com.example.javaspringbootb30.repository.TokenConfirmRepository;
import com.example.javaspringbootb30.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticattionManger;
    private final HttpSession session;
    private final TokenConfirmRepository tokenConfirmRepository;
    private final MailService mailService;
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
        } catch (DisabledException e){
            throw new BadRequestException("Tài khoản của bạn chưa được kích hoạt vui lòng kiểm tra email");
        } catch (AuthenticationException e){
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
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .enable(false)
                .build();
        userRepository.save(user);

        TokenConfirm tokenConfirm=TokenConfirm.builder()
                .token(UUID.randomUUID().toString())
                .type(TokenType.CONFIRM_REGISTRATION)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(1))
                .user(user)
                .build();
        tokenConfirmRepository.save(tokenConfirm);

        String link="http://localhost:8090/xac-thuc-tai-khoan?token="+tokenConfirm.getToken();
        mailService.sendMail(user.getEmail(),"Xác thực tài khoản",link);
    }
    public VerifyResponse verifyAccount(String token){
        Optional<TokenConfirm> optionalTokenConfirm=tokenConfirmRepository
                .findByTokenAndType(token,TokenType.CONFIRM_REGISTRATION);

        if (optionalTokenConfirm.isEmpty()){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Token khong hop le")
                    .build();
        }
        TokenConfirm tokenConfirm=optionalTokenConfirm.get();
        if (tokenConfirm.getConfirmedAt()!=null){
            return VerifyResponse.builder()
                    .success(false)
                    .message("Token da duoc xac thuc")
                    .build();
        }

        if (tokenConfirm.getExpiredAt().isBefore(LocalDateTime.now())) {
            return VerifyResponse.builder()
                    .success(false)
                    .message("Token da het han")
                    .build();
        }
        User user=tokenConfirm.getUser();
        user.setEnable(true);

        tokenConfirm.setConfirmedAt(LocalDateTime.now());
        tokenConfirmRepository.save(tokenConfirm);
        return VerifyResponse.builder()
                .success(true)
                .message("Xac thuc thanh cong")
                .build();
    }
}
