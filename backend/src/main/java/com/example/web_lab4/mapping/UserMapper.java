package com.example.web_lab4.mapping;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.dto.response.UserResponseDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.entity.enums.Role;
import com.example.web_lab4.repository.RoleRepository;
import com.example.web_lab4.service.RoleService;
import com.example.web_lab4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserEntity toEntity(UserRequestDto userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDTO.getName());
        userEntity.setPassword(userRequestDTO.getPassword());
        userEntity.setRole(null);
        return userEntity;
    }

    public UserResponseDto toResponseDTO(UserEntity userEntity) {
        return new UserResponseDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getRole().getName()
        );
    }

    public List<UserResponseDto> toListOfResponseDTO(List<UserEntity> userEntityList) {
        return userEntityList.stream().map(this::toResponseDTO).toList();
    }
}
