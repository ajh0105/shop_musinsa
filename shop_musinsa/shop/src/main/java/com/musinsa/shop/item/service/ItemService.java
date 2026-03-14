package com.musinsa.shop.item.service;

import com.musinsa.shop.item.dto.ItemRead;
import java.util.List;

public interface ItemService {

    //전체 상품 목록 조회
    List<ItemRead> findAll();

    //상품 목록 조회(특정 아이디 리스트로 조회)
    List<ItemRead> findAll(List<Integer> ids);

    //카테고리 조회(특정 카테고리로 조회)
    List<ItemRead> findByCategory(List<String> cts);
}
