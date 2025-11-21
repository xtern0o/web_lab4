package com.example.web_lab4.service;

import com.example.web_lab4.entity.PermissionEntity;
import com.example.web_lab4.entity.RoleEntity;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.repository.RoleRepository;
import com.example.web_lab4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Set<PermissionEntity> getAllPermissionsForRole(String roleName) {
        Optional<RoleEntity> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) return Set.of();
        return role.get().getPermissions();
    }

    public Optional<RoleEntity> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
