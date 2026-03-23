package com.ventalize.shop.cart.dto;

import com.ventalize.shop.item.dto.ItemRead;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {

    private Integer id;       // cart row id
    private Integer itemId;
    private Integer quantity;
    private ItemRead item;
}
