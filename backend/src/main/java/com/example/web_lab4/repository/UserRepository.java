package com.example.web_lab4.repository;

import com.example.web_lab4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByName(String name);
    boolean existsByKeycloakId(UUID keycloakId);
    Optional<UserEntity> findByName(String name);
    Optional<UserEntity> findByKeycloakId(UUID keycloakId);
}
