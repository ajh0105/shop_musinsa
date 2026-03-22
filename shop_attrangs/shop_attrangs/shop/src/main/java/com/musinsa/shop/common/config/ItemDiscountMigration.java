package com.musinsa.shop.common.config;

import com.musinsa.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class ItemDiscountMigration implements CommandLineRunner {

    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) {
        long fixed = itemRepository.findAll().stream()
                .filter(item -> !"오늘의 할인".equals(item.getCategory()) && item.getDiscountPer() != null && item.getDiscountPer() > 0)
                .peek(item -> item.setDiscountPer(0))
                .map(itemRepository::save)
                .count();

        if (fixed > 0) {
            log.info("할인율 일괄 초기화: '오늘의 할인' 외 {}개 상품의 할인율을 0으로 수정", fixed);
        }
    }
}
