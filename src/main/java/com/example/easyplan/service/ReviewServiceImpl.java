package com.example.easyplan.service;

import com.example.easyplan.controller.ReviewForm;
import com.example.easyplan.domain.dto.ResponseReview;
import com.example.easyplan.domain.dto.ReviewUpdateRequest;
import com.example.easyplan.domain.entity.review.Comment;
import com.example.easyplan.domain.entity.review.Photo;
import com.example.easyplan.domain.entity.review.Review;
import com.example.easyplan.domain.entity.user.User;
import com.example.easyplan.repository.PhotoRepository;
import com.example.easyplan.repository.ReviewRepository;
import com.example.easyplan.repository.ReviewsRepository;
import com.example.easyplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;


@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewsRepository repository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PhotoRepository photoRepository;
    private final HeartService heartService;

    @Override
    @Transactional
    public void save(ReviewForm reviewForm, List<MultipartFile> photoFiles, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        String title = reviewForm.getTitle();
        String content = reviewForm.getContent();
        Review review = new Review();
        review.setTitle(title);
        review.setContent(content);
        review.setUser(user);

        Review saveReview = repository.save(review);

        log.info("review_id: " + review.getId());

        if (photoFiles != null && !photoFiles.isEmpty()) {
            for (MultipartFile file : photoFiles) {
                String[] fileNamePath = fileStorageService.storeFile(file, saveReview.getId());
                Photo photo = new Photo();
                photo.setFileName(fileNamePath[0]);
                photo.setFilePath(fileNamePath[1]);
                log.info("photo_file_name: " + fileNamePath[0]);
                saveReview.addPhoto(photo);
            }
        }

    }

    @Override
    @Transactional
    public void save(ReviewForm reviewForm, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        String title = reviewForm.getTitle();
        String content = reviewForm.getContent();
        Review review = new Review();
        review.setTitle(title);
        review.setContent(content);
        review.setUser(user);
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public boolean updateReview(Long userId, Long reviewId, ReviewForm reviewForm, List<MultipartFile> photoFiles) {
        User user = userRepository.findById(userId).get();
        Review review = reviewRepository.findByReviewId(reviewId);
        if (review == null || !review.getUser().getId().equals(userId)) {
            return false;
        }
        review.setTitle(reviewForm.getTitle());
        review.setContent(reviewForm.getContent());
        List<Photo> photos = review.getPhotos();
        for (Photo photo : photos) {
            photoRepository.deleteById(photo.getId());
        }
        review.getPhotos().clear();
        if (photoFiles != null && !photoFiles.isEmpty()) {
            for (MultipartFile file : photoFiles) {
                String[] fileNamePath = fileStorageService.storeFile(file, review.getId());
                Photo photo = new Photo();
                photo.setFileName(fileNamePath[0]);
                photo.setFilePath(fileNamePath[1]);
                log.info("photo_file_name: " + fileNamePath[0]);
                review.addPhoto(photo);
            }
        }
        reviewRepository.save(review);
        return true;
    }

    @Override
    @Transactional
    public boolean updateReview(Long userId, Long reviewId, ReviewForm reviewForm) {
        User user = userRepository.findById(userId).orElse(null);
        Review review = reviewRepository.findByReviewId(reviewId);
        if (review == null || !review.getUser().getId().equals(userId)) {
            return false;
        }
        review.setTitle(reviewForm.getTitle());
        review.setContent(reviewForm.getContent());
        List<Photo> photos = review.getPhotos();
        for (Photo photo : photos) {
            photoRepository.deleteById(photo.getId());
        }
        review.getPhotos().clear();
        reviewRepository.save(review);
        return true;
    }

    @Override
    public ResponseReview findReviewByReviewId(Long reviewId) {
        Review review = repository.findById(reviewId).get();
        ResponseReview responseReview = new ResponseReview();
        responseReview.setId(review.getId());
        responseReview.setTitle(review.getTitle());
        responseReview.setPhotos(review.getPhotos());
        responseReview.setContent(review.getContent());
        responseReview.setHeartList(review.getHeartList());
        responseReview.setViewCount(review.getViewCount());
        responseReview.setUserId(review.getUser().getId());
        responseReview.setCommentList(review.getCommentList().stream().map(Comment::toDTO).toList());
        return responseReview;
    }

    @Override
    @Transactional
    public List<ResponseReview> findReviewsByUserId(Long userId) {
        List<ResponseReview> newReview = new ArrayList<>();
        List<Review> myReviews = reviewRepository.findReviewsByUserId(userId);
        for (Review review : myReviews) {
            ResponseReview responseReview = new ResponseReview();
            responseReview.setId(review.getId());
            responseReview.setTitle(review.getTitle());
            responseReview.setPhotos(review.getPhotos());
            responseReview.setContent(review.getContent());
            responseReview.setViewCount(review.getViewCount());
            responseReview.setHeartList(review.getHeartList());
            responseReview.setUserId(review.getUser().getId());
            newReview.add(responseReview);
        }
        return newReview;
    }

    @Override
    @Transactional
    public boolean deleteReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId).orElse(null);
        Review review = reviewRepository.findByReviewId(reviewId);
        if (review == null || !review.getUser().getId().equals(userId)) {
            return false;
        }
        reviewRepository.delete(review);
        return true;
    }

    @Transactional(readOnly = true)
    public Page<ResponseReview> findAllReviews(PageRequest pageRequest) {
        Page<Review> reviewPage = reviewRepository.findAll(pageRequest);
        List<ResponseReview> reviews = reviewPage.stream()
                .map(ResponseReview::new)
                .collect(Collectors.toList());

        return new PageImpl<>(reviews, pageRequest, reviewPage.getTotalElements());
    }

}

