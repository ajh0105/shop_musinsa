package com.musinsa.shop.wishlist.dto;

import com.musinsa.shop.wishlist.entity.Wishlist;
import lombok.Getter;

@Getter
public class WishlistRequest {
    private Integer itemId;

    public Wishlist toEntity(Integer memberId) {
        return new Wishlist(memberId, itemId);
    }
}
