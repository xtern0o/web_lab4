package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.PointRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class AreaCheckService {
    public boolean isHit(PointRequestDTO pointRequestDTO) {
        float x = pointRequestDTO.getX();
        float y = pointRequestDTO.getY();
        float r = pointRequestDTO.getR();

        return true;
    }
}
