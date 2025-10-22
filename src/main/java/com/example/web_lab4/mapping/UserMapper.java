package com.example.web_lab4.mapping;

import com.example.web_lab4.dto.request.UserRequestDTO;
import com.example.web_lab4.dto.response.UserResponseDTO;
import com.example.web_lab4.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserEntity toEntity(UserRequestDTO userRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDTO.getName());
        userEntity.setPassword(userRequestDTO.getPassword());
        return userEntity;
    }

    public UserResponseDTO toResponseDTO(UserEntity userEntity) {
        return new UserResponseDTO(
                userEntity.getId(),
                userEntity.getName()
        );
    }

    public List<UserResponseDTO> toListOfResponseDTO(List<UserEntity> userEntityList) {
        return userEntityList.stream().map(this::toResponseDTO).toList();
    }
}
