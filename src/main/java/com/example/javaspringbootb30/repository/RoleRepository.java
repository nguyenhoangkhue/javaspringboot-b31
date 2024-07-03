package com.example.javaspringbootb30.repository;

import com.example.javaspringbootb30.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String user);
}
