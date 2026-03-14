package com.musinsa.shop.item.entity;

import com.musinsa.shop.item.dto.ItemRead;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 30)
    private String category;

    @Column(nullable = false, length = 100)
    private String imgPath;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer discountPer;

    @Column(name = "stock_count")
    @ColumnDefault("0")
    private Integer stockCount;

    @Column(name = "view_count")
    @ColumnDefault("0")
    private Integer viewCount;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    //상품 조회 DTO로 변환
    public ItemRead toRead() {
        return ItemRead.builder()
                .id(id)
                .brand(brand)
                .name(name)
                .category(category)
                .imgPath(imgPath)
                .price(price)
                .discountPer(discountPer)
                // 비즈니스 로직 추가
                .salePrice(price * (100 - discountPer) / 100)
                .viewCount(viewCount)
                .isSoldOut(stockCount != null && stockCount <= 0)
                .build();
    }
}
