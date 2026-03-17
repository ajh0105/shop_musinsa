package com.musinsa.shop.faq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqCreateRequest {
    private String category;
    private String question;
    private String answer;
    private Integer sortOrder;
}
