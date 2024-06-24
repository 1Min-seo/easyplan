package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.review.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
