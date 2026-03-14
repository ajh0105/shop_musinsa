package com.musinsa.shop.item.service;

import com.musinsa.shop.item.dto.ItemRead;
import com.musinsa.shop.item.entity.Item;
import com.musinsa.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseItemService implements ItemService{

    private final ItemRepository itemRepository;

    //전체 상품 목록 조회
    @Override
    public List<ItemRead> findAll() {
        return itemRepository.findAll().stream().map(Item::toRead).toList();
    }

    //상품 목록 조회(특정 아이디 리스트로 조회)
    @Override
    public List<ItemRead> findAll(List<Integer> ids) {
        return itemRepository.findAllByIdIn(ids).stream().map(Item::toRead).toList();
    }

    //카테고리 목록 조회(특정 카테고리로 조회)
    @Override
    public List<ItemRead> findByCategory(List<String> cts) {
        return itemRepository.findAllByCategoryIn(cts).stream().map(Item::toRead).toList();
    }
}
