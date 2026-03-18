package com.musinsa.shop.item.repository;

import com.musinsa.shop.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByIdIn(List<Integer> ids);

    List<Item> findAllByCategoryIn(List<String> cts);

    List<Item> findTop5ByOrderByCreatedAtDesc();

    List<Item> findTop5ByOrderByViewCountDesc();

    List<Item> findTop5ByDiscountPerGreaterThanOrderByDiscountPerDesc(int discountPer);

    List<Item> findByCategoryAndIdNot(String category, Integer id);

    List<Item> findByNameContainingOrBrandContaining(String name, String brand);
}
