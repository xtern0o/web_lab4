package com.example.web_lab4.repository;

import com.example.web_lab4.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Long> {
    List<PointEntity> findAllByUserId(Long id);
}
