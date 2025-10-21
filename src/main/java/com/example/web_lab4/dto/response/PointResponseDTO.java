package com.example.web_lab4.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointResponseDTO {
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
    private Long userId;

    @NotNull
    private Instant createdAt;

}