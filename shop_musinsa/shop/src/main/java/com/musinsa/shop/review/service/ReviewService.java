package com.musinsa.shop.review.service;

import com.musinsa.shop.review.dto.ReviewRead;
import com.musinsa.shop.review.dto.ReviewRequest;
import com.musinsa.shop.review.entity.Review;

import java.util.List;

public interface ReviewService {
    List<ReviewRead> findByItem(Integer itemId);
    List<ReviewRead> findByMember(Integer memberId);
    ReviewRead save(ReviewRequest req, Integer memberId);
    ReviewRead update(Integer id, ReviewRequest req, Integer memberId);
    boolean delete(Integer id, Integer memberId);
    List<ReviewRead> findAll();
    boolean adminDelete(Integer id);
}
