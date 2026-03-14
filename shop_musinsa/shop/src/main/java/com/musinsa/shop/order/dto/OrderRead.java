package com.musinsa.shop.order.dto;

import com.musinsa.shop.item.dto.ItemRead;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderRead {

    private Integer id;
    private String name;
    private String address;
    private String payment;
    private Long amount;
    private LocalDateTime createdAt;
    private List<ItemRead> items;
}
