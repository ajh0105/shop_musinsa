package com.musinsa.shop.faq.service;

import com.musinsa.shop.faq.dto.FaqCreateRequest;
import com.musinsa.shop.faq.dto.FaqRead;

import java.util.List;

public interface FaqService {
    List<FaqRead> findAll();
    List<FaqRead> findByCategory(String category);
    FaqRead save(FaqCreateRequest req);
    FaqRead update(Integer id, FaqCreateRequest req);
    boolean delete(Integer id);
}
