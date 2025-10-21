package com.example.web_lab4.repository;

import com.example.web_lab4.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
    List<PointEntity> findAllByUserId(Long id);
}
