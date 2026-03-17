package com.musinsa.shop.wishlist.entity;

import com.musinsa.shop.wishlist.dto.WishlistRead;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false)
    private Integer itemId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Wishlist() {}

    public Wishlist(Integer memberId, Integer itemId) {
        this.memberId = memberId;
        this.itemId = itemId;
    }

    public WishlistRead toRead() {
        return WishlistRead.builder()
                .id(id)
                .itemId(itemId)
                .build();
    }
}
