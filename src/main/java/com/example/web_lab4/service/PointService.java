package com.example.web_lab4.service;

import com.example.web_lab4.dto.response.PointResponseDTO;
import com.example.web_lab4.mapping.PointMapper;
import com.example.web_lab4.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointMapper pointMapper;

    List<PointResponseDTO> getAllPoints() {
        return pointMapper.toListOfResponseDTO(pointRepository.findAll());
    }

    List<PointResponseDTO> getAllPointsByUserId(Long id) {
        return pointMapper.toListOfResponseDTO(pointRepository.findAllByUserId(id));
    }
}
