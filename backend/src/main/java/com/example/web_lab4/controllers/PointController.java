package com.example.web_lab4.controllers;

import com.example.web_lab4.annotations.HasPermission;
import com.example.web_lab4.dto.groups.OnCreate;
import com.example.web_lab4.dto.request.PointRequestDto;
import com.example.web_lab4.dto.response.PointResponseDto;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
public class PointController {
    @Autowired
    private PointService pointService;

    @GetMapping
    @HasPermission({"points:read"})
    public List<PointResponseDto> getPoints(
            @RequestParam(name = "user_id", required = false) Long userId
    ) {
        if (userId != null) return pointService.getAllPointsByUserId(userId);
        return pointService.getAllPoints();
    }

    @PostMapping
    @HasPermission({"points:create"})
    public PointResponseDto createPoint(
            @Validated(OnCreate.class) @RequestBody PointRequestDto requestDTO,
            @AuthenticationPrincipal UserDetails currentUser
    ) {
        return pointService.createPoint(requestDTO, (UserEntity) currentUser);
    }
}
