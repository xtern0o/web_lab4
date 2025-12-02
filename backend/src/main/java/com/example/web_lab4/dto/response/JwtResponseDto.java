package com.example.web_lab4.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto {
    @NotNull
    private String token;

    @NotNull
    private UUID userId;

    @NotNull
    private String username;
}
