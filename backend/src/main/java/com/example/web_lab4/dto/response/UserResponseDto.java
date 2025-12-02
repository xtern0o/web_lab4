package com.example.web_lab4.dto.response;

import com.example.web_lab4.entity.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String roleName;
}
