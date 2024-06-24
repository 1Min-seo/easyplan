package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Review, Long> {
}