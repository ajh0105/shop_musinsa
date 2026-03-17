package com.musinsa.shop.wishlist.service;

import com.musinsa.shop.wishlist.dto.WishlistRead;
import com.musinsa.shop.wishlist.entity.Wishlist;
import com.musinsa.shop.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseWishlistService implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public List<WishlistRead> findAll(Integer memberId) {
        return wishlistRepository.findAllByMemberId(memberId).stream()
                .map(Wishlist::toRead)
                .toList();
    }

    @Override
    public WishlistRead find(Integer memberId, Integer itemId) {
        Optional<Wishlist> opt = wishlistRepository.findByMemberIdAndItemId(memberId, itemId);
        return opt.map(Wishlist::toRead).orElse(null);
    }

    @Override
    public void save(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public void remove(Integer memberId, Integer itemId) {
        wishlistRepository.deleteByMemberIdAndItemId(memberId, itemId);
    }
}
