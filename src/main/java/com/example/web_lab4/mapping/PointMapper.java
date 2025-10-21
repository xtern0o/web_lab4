package com.example.web_lab4.mapping;

import com.example.web_lab4.dto.request.PointRequestDTO;
import com.example.web_lab4.dto.response.PointResponseDTO;
import com.example.web_lab4.entity.PointEntity;
import com.example.web_lab4.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PointMapper {
    public PointEntity toEntity(PointRequestDTO pointDTO, UserEntity userEntity, boolean hit) {
        PointEntity pointEntity = new PointEntity();
        pointEntity.setX(pointDTO.getX());
        pointEntity.setY(pointDTO.getY());
        pointEntity.setR(pointDTO.getR());
        pointEntity.setHit(hit);
        pointEntity.setUser(userEntity);
        return pointEntity;
    }

    public PointResponseDTO toResponseDTO(PointEntity pointEntity) {
        return new PointResponseDTO(
                pointEntity.getId(),
                pointEntity.getX(),
                pointEntity.getY(),
                pointEntity.getR(),
                pointEntity.getHit(),
                pointEntity.getUser().getId(),
                pointEntity.getCreatedAt()
        );
    }

    public List<PointResponseDTO> toListOfResponseDTO(List<PointEntity> pointEntities) {
        return pointEntities.stream().map(this::toResponseDTO).toList();
    }

}
