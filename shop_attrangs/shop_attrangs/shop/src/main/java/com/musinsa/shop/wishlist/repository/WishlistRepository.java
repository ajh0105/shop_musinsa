package com.musinsa.shop.wishlist.repository;

import com.musinsa.shop.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    List<Wishlist> findAllByMemberId(Integer memberId);

    Optional<Wishlist> findByMemberIdAndItemId(Integer memberId, Integer itemId);

    void deleteByMemberIdAndItemId(Integer memberId, Integer itemId);
}
