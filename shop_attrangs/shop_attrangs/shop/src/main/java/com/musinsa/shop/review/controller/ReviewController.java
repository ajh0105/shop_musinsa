package com.musinsa.shop.review.controller;

import com.musinsa.shop.common.util.SecurityUtil;
import com.musinsa.shop.review.dto.ReviewRead;
import com.musinsa.shop.review.dto.ReviewRequest;
import com.musinsa.shop.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtil securityUtil;

    /** 전체 리뷰 목록 (커뮤니티 페이지용) */
    @GetMapping("/all")
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    /** 상품별 리뷰 목록 */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> byItem(@PathVariable Integer itemId) {
        return ResponseEntity.ok(reviewService.findByItem(itemId));
    }

    /** 내 리뷰 목록 */
    @GetMapping("/my")
    public ResponseEntity<?> my() {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(reviewService.findByMember(memberId));
    }

    /** 리뷰 작성 */
    @PostMapping
    public ResponseEntity<?> add(@RequestBody ReviewRequest req) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        ReviewRead review = reviewService.save(req, memberId);
        return ResponseEntity.ok(review);
    }

    /** 리뷰 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ReviewRequest req) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        ReviewRead review = reviewService.update(id, req, memberId);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    /** 리뷰 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        return reviewService.delete(id, memberId)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
