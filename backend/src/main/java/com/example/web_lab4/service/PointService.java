package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.PointRequestDto;
import com.example.web_lab4.dto.response.PointResponseDto;
import com.example.web_lab4.entity.PointEntity;
import com.example.web_lab4.entity.UserEntity;
import com.example.web_lab4.mapping.PointMapper;
import com.example.web_lab4.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointMapper pointMapper;
    private final AreaCheckService areaCheckService;

    public List<PointResponseDto> getAllPoints() {
        return pointMapper.toListOfResponseDTO(pointRepository.findAll());
    }

    public List<PointResponseDto> getAllPointsByUserId(UUID id) {
        return pointMapper.toListOfResponseDTO(pointRepository.findAllByUserId(id));
    }

    public PointResponseDto createPoint(PointRequestDto pointRequestDTO, UserEntity userEntity) {
        PointEntity savedPoint = pointRepository.save(
                pointMapper.toEntity(
                        pointRequestDTO,
                        userEntity,
                        areaCheckService.isHit(pointRequestDTO)
                )
        );
        return pointMapper.toResponseDTO(savedPoint);
    }
}
