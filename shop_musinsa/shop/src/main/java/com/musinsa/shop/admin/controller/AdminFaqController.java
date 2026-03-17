package com.musinsa.shop.admin.controller;

import com.musinsa.shop.faq.dto.FaqCreateRequest;
import com.musinsa.shop.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/admin/faq")
@RequiredArgsConstructor
public class AdminFaqController {

    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<?> readAll() {
        return ResponseEntity.ok(faqService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody FaqCreateRequest req) {
        if (req.getQuestion() == null || req.getQuestion().isBlank() ||
                req.getAnswer() == null || req.getAnswer().isBlank()) {
            return ResponseEntity.badRequest().body("질문과 답변은 필수입니다.");
        }
        return ResponseEntity.ok(faqService.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody FaqCreateRequest req) {
        var result = faqService.update(id, req);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return faqService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
