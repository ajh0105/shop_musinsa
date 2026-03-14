package com.musinsa.shop.item.repository;

import com.musinsa.shop.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    //여러 아이디로 상품을 조회
    List<Item> findAllByIdIn(List<Integer> ids);

    //특정 카테고리로 조회
    List<Item> findAllByCategoryIn(List<String> cts);
}
