package com.musinsa.shop.common.config;

import com.musinsa.shop.item.entity.Item;
import com.musinsa.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * DB에 picsum.photos 이미지가 있을 경우, 카테고리에 맞는 패션 이미지로 자동 교체합니다.
 * 앱 시작 시 한 번만 실행되며, 이미 교체된 경우 스킵합니다.
 * 이미지 출처: Unsplash (https://unsplash.com) — 무료 상업 이용 가능
 */
@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class ItemImageMigration implements CommandLineRunner {

    private final ItemRepository itemRepository;

    // 카테고리별 패션 이미지 URL 목록 (Unsplash 무료 이미지)
    private static final Map<String, List<String>> CATEGORY_IMAGES = Map.of(
        "OUTER", List.of(
            "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1548126032-079a0fb0099d?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1608063615781-e2ef8c73d114?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1551537482-f2075a1d41f2?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1578932750294-f5075e85f44a?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1544923246-77307dd654cb?w=400&h=500&fit=crop&auto=format"
        ),
        "TOP", List.of(
            "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1503341455253-b2e723bb3dbb?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1620799139834-6b8f844fbe61?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1559163499-413811fb2344?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1516478177764-9fe5bd7e9717?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1556821840-3a63f15732ce?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1583744946564-b52d01e7f922?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400&h=500&fit=crop&auto=format"
        ),
        "PANTS", List.of(
            "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1594938298603-c8148c4b4d17?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1600717535275-0b18ede2f7fc?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1547257965-83e0b749c4b4?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400&h=500&fit=crop&auto=format"
        ),
        "SHOES", List.of(
            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1539185441755-769473a23570?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1465453869711-7e174808ace9?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=400&h=500&fit=crop&auto=format"
        ),
        "BAG", List.of(
            "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1622560480605-d83c853bc5c3?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1566150905458-1bf1fc113f0d?w=400&h=500&fit=crop&auto=format"
        ),
        "ACC", List.of(
            "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1509695507497-903c140c43b0?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1576871337622-98d48d1cf531?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1588850561407-ed78c282e89b?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1601924994987-69e26d50dc26?w=400&h=500&fit=crop&auto=format"
        ),
        "OUTLET", List.of(
            "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1503341455253-b2e723bb3dbb?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=500&fit=crop&auto=format",
            "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400&h=500&fit=crop&auto=format"
        )
    );

    @Override
    public void run(String... args) {
        List<Item> allItems = itemRepository.findAll();

        // picsum.photos URL이 있는 경우에만 마이그레이션 실행
        boolean needsMigration = allItems.stream()
                .anyMatch(item -> item.getImgPath() != null && item.getImgPath().contains("picsum.photos"));

        if (!needsMigration) return;

        int updatedCount = 0;

        for (Map.Entry<String, List<String>> entry : CATEGORY_IMAGES.entrySet()) {
            String category = entry.getKey();
            List<String> imageUrls = entry.getValue();

            List<Item> categoryItems = allItems.stream()
                    .filter(item -> category.equals(item.getCategory()))
                    .toList();

            for (int i = 0; i < categoryItems.size(); i++) {
                Item item = categoryItems.get(i);
                item.setImgPath(imageUrls.get(i % imageUrls.size()));
                updatedCount++;
            }
        }

        itemRepository.saveAll(allItems);
        log.info("✅ 상품 이미지 카테고리별 교체 완료: {}개 상품 업데이트", updatedCount);
    }
}
