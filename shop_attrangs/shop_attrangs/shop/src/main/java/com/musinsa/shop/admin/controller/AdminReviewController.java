package com.musinsa.shop.admin.controller;

import com.musinsa.shop.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return reviewService.adminDelete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
