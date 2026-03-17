package com.musinsa.shop.faq.service;

import com.musinsa.shop.faq.dto.FaqCreateRequest;
import com.musinsa.shop.faq.dto.FaqRead;
import com.musinsa.shop.faq.entity.Faq;
import com.musinsa.shop.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseFaqService implements FaqService {

    private final FaqRepository faqRepository;

    private FaqRead toRead(Faq f) {
        return FaqRead.builder()
                .id(f.getId())
                .category(f.getCategory())
                .question(f.getQuestion())
                .answer(f.getAnswer())
                .sortOrder(f.getSortOrder())
                .createdAt(f.getCreatedAt())
                .updatedAt(f.getUpdatedAt())
                .build();
    }

    @Override
    public List<FaqRead> findAll() {
        return faqRepository.findAllByOrderBySortOrderAscCreatedAtDesc()
                .stream().map(this::toRead).toList();
    }

    @Override
    public List<FaqRead> findByCategory(String category) {
        return faqRepository.findAllByCategoryOrderBySortOrderAscCreatedAtDesc(category)
                .stream().map(this::toRead).toList();
    }

    @Override
    public FaqRead save(FaqCreateRequest req) {
        Faq faq = Faq.builder()
                .category(req.getCategory() != null ? req.getCategory() : "일반")
                .question(req.getQuestion())
                .answer(req.getAnswer())
                .sortOrder(req.getSortOrder() != null ? req.getSortOrder() : 0)
                .build();
        return toRead(faqRepository.save(faq));
    }

    @Override
    public FaqRead update(Integer id, FaqCreateRequest req) {
        return faqRepository.findById(id).map(f -> {
            if (req.getCategory() != null) f.setCategory(req.getCategory());
            if (req.getQuestion() != null) f.setQuestion(req.getQuestion());
            if (req.getAnswer() != null) f.setAnswer(req.getAnswer());
            if (req.getSortOrder() != null) f.setSortOrder(req.getSortOrder());
            return toRead(faqRepository.save(f));
        }).orElse(null);
    }

    @Override
    public boolean delete(Integer id) {
        return faqRepository.findById(id).map(f -> {
            faqRepository.delete(f);
            return true;
        }).orElse(false);
    }
}
