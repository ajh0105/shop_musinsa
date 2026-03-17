package com.musinsa.shop.faq.repository;

import com.musinsa.shop.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
    List<Faq> findAllByCategoryOrderBySortOrderAscCreatedAtDesc(String category);
    List<Faq> findAllByOrderBySortOrderAscCreatedAtDesc();
}
