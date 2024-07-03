package com.example.javaspringbootb30.entity;

import com.example.javaspringbootb30.model.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token_confirm")
public class TokenConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "token")
    private String token; // chuỗi token

    @Column(name = "created_at")
    private LocalDateTime createdAt; // thời gian tạo

    @Column(name = "expired_at")
    private LocalDateTime expiredAt; // thời gian hết hạn

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt; // thời gian xác thực

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // token này của user nào

    @Enumerated(EnumType.STRING)
    TokenType type;
}
