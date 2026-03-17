package com.musinsa.shop.wishlist.dto;

import com.musinsa.shop.item.dto.ItemRead;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistItemResponse {
    private Integer id;
    private Integer itemId;
    private ItemRead item;
}
