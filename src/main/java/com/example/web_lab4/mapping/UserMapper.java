package com.example.web_lab4.mapping;

import com.example.web_lab4.dto.request.UserRequestDto;
import com.example.web_lab4.dto.response.UserResponseDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.entity.enums.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserEntity toEntity(UserRequestDto userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDTO.getName());
        userEntity.setPassword(userRequestDTO.getPassword());
        userEntity.setRole(Role.ROLE_USER);
        return userEntity;
    }

    public UserResponseDto toResponseDTO(UserEntity userEntity) {
        return new UserResponseDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getRole()
        );
    }

    public List<UserResponseDto> toListOfResponseDTO(List<UserEntity> userEntityList) {
        return userEntityList.stream().map(this::toResponseDTO).toList();
    }
}
