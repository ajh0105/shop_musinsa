package com.musinsa.shop.wishlist.controller;

import com.musinsa.shop.common.util.SecurityUtil;
import com.musinsa.shop.item.dto.ItemRead;
import com.musinsa.shop.item.service.ItemService;
import com.musinsa.shop.wishlist.dto.WishlistItemResponse;
import com.musinsa.shop.wishlist.dto.WishlistRead;
import com.musinsa.shop.wishlist.dto.WishlistRequest;
import com.musinsa.shop.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final ItemService itemService;
    private final SecurityUtil securityUtil;

    // 찜 목록 조회 (상품 상세 포함)
    @GetMapping("/api/wishlist/items")
    public ResponseEntity<?> readAll() {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();

        List<WishlistRead> wishlists = wishlistService.findAll(memberId);
        List<Integer> itemIds = wishlists.stream().map(WishlistRead::getItemId).toList();
        List<ItemRead> items = itemService.findAll(itemIds);

        Map<Integer, ItemRead> itemMap = items.stream()
                .collect(Collectors.toMap(ItemRead::getId, i -> i));

        List<WishlistItemResponse> response = wishlists.stream()
                .map(w -> WishlistItemResponse.builder()
                        .id(w.getId())
                        .itemId(w.getItemId())
                        .item(itemMap.get(w.getItemId()))
                        .build())
                .toList();

        return ResponseEntity.ok(response);
    }

    // 찜한 상품 ID 목록 (프론트 초기화용)
    @GetMapping("/api/wishlist/ids")
    public ResponseEntity<?> readIds() {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();

        List<Integer> ids = wishlistService.findAll(memberId).stream()
                .map(WishlistRead::getItemId)
                .toList();
        return ResponseEntity.ok(ids);
    }

    // 찜 토글 (추가 / 취소)
    @PostMapping("/api/wishlist/toggle")
    public ResponseEntity<?> toggle(@RequestBody WishlistRequest req) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();

        WishlistRead existing = wishlistService.find(memberId, req.getItemId());
        if (existing == null) {
            wishlistService.save(req.toEntity(memberId));
            return ResponseEntity.ok(Map.of("wished", true));
        } else {
            wishlistService.remove(memberId, req.getItemId());
            return ResponseEntity.ok(Map.of("wished", false));
        }
    }
}
