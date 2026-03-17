package com.musinsa.shop.faq.controller;

import com.musinsa.shop.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /** 전체 FAQ 목록 */
    @GetMapping
    public ResponseEntity<?> readAll(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(faqService.findByCategory(category));
        }
        return ResponseEntity.ok(faqService.findAll());
    }
}
