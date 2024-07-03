package com.example.javaspringbootb30.repository;

import com.example.javaspringbootb30.entity.TokenConfirm;
import com.example.javaspringbootb30.model.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfirmRepository extends JpaRepository<TokenConfirm,Integer> {
    Optional<TokenConfirm> findByTokenAndType(String token, TokenType type);
}
