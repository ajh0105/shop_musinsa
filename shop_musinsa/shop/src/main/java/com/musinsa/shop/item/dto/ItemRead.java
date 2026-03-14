package com.musinsa.shop.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemRead {

    private final Integer id;
    private final String brand;
    private final String name;
    private final String category;
    private final String imgPath;
    private final Integer price;
    private final Integer discountPer;
    private final Integer salePrice;    // 계산된 가격
    private final Integer viewCount;    // 인기 표시용
    private final boolean isSoldOut;    // 재고 기반 품절 여부
}
