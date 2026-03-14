package com.musinsa.shop.item.controller;

import com.musinsa.shop.item.dto.ItemRead;
import com.musinsa.shop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/api/items")
    public ResponseEntity<?> readAll(@RequestParam(required = false) String category) {
        List<ItemRead> items;
        if (StringUtils.hasLength(category)) {
            items = itemService.findByCategory(List.of(category));
        } else {
            items = itemService.findAll();
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
