package com.example.web_lab4.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointResponseDto {
    @NotNull
    private Long id;

    @NotNull
    private Float x;

    @NotNull
    private Float y;

    @NotNull
    private Float r;

    @NotNull
    private boolean hit;

    @NotNull
    private UUID userId;

    @NotNull
    private Instant createdAt;

}