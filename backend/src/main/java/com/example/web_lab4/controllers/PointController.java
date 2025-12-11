package com.example.web_lab4.controllers;

import com.example.web_lab4.dto.groups.OnCreate;
import com.example.web_lab4.dto.request.PointRequestDto;
import com.example.web_lab4.dto.response.PointResponseDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.service.PointService;
import com.example.web_lab4.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private final UserService userService;

    @GetMapping
    public List<PointResponseDto> getPoints(
            @RequestParam(name = "user_id", required = false) UUID userId
    ) {
        if (userId != null) return pointService.getAllPointsByUserId(userId);
        return pointService.getAllPoints();
    }

    @PostMapping
    public PointResponseDto createPoint(
            @Validated(OnCreate.class) @RequestBody PointRequestDto requestDTO
    ) {
        UserEntity currentUser = userService.getCurrentUser();
        return pointService.createPoint(requestDTO, currentUser);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllPoints() {
        pointService.deleteAllPoints();
    }

}
