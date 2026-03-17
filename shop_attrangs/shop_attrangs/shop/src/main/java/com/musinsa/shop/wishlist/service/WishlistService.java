package com.musinsa.shop.wishlist.service;

import com.musinsa.shop.wishlist.dto.WishlistRead;
import com.musinsa.shop.wishlist.entity.Wishlist;

import java.util.List;

public interface WishlistService {

    List<WishlistRead> findAll(Integer memberId);

    WishlistRead find(Integer memberId, Integer itemId);

    void save(Wishlist wishlist);

    void remove(Integer memberId, Integer itemId);
}
