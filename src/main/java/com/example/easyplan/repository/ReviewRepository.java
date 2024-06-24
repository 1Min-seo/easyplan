package com.example.easyplan.repository;

import com.example.easyplan.domain.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepository {
    public void save(Review review);
    public Review findReviewId(Long reviewId);
    public Review findByReviewId(Long reviewId);
    public List<Review> findReviewsByUserId(Long userId);
    public void delete(Review review);
    public Page<Review> findAll(Pageable pageable);

}
