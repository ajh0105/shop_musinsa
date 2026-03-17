package com.musinsa.shop.wishlist.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistRead {
    private Integer id;
    private Integer itemId;
}
