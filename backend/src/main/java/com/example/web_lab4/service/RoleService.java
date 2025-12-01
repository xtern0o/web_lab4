package com.example.web_lab4.service;

import com.example.web_lab4.entity.RoleEntity;
import com.example.web_lab4.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<RoleEntity> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}