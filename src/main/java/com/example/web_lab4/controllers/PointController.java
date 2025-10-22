package com.example.web_lab4.controllers;

import com.example.web_lab4.dto.groups.OnCreate;
import com.example.web_lab4.dto.request.PointRequestDTO;
import com.example.web_lab4.dto.response.PointResponseDTO;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @GetMapping
    public List<PointResponseDTO> getPoints(
            @RequestParam(name = "user_id", required = false) Long userId
    ) {
        if (userId != null) return pointService.getAllPointsByUserId(userId);

        return pointService.getAllPoints();
    }

    @PostMapping
    public PointResponseDTO createPoint(
            @Validated(OnCreate.class) @RequestParam PointRequestDTO requestDTO,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        return pointService.createPoint(requestDTO, currentUser);
    }
}
