package com.example.web_lab4.service;

import com.example.web_lab4.dto.request.PointRequestDto;
import org.springframework.stereotype.Service;

@Service
public class AreaCheckService {
    public boolean isHit(PointRequestDto pointRequestDTO) {
        float x = pointRequestDTO.getX();
        float y = pointRequestDTO.getY();
        float r = pointRequestDTO.getR();

        if (x > 0 && y > 0) return false;
        else if (x <= 0 && y > 0) return y <= x + r / 2;
        else if (x <= 0 && y <= 0) return x >= -r && y >= -r / 2;
        else return x * x + y * y <= (r / 2) * (r / 2);

    }
}
