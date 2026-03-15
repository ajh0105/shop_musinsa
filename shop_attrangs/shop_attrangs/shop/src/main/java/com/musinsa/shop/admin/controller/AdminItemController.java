package com.musinsa.shop.admin.controller;

import com.musinsa.shop.common.config.FileStorageService;
import com.musinsa.shop.item.dto.ItemCreateRequest;
import com.musinsa.shop.item.entity.Item;
import com.musinsa.shop.item.repository.ItemRepository;
import com.musinsa.shop.item.support.ItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/admin/items")
@RequiredArgsConstructor
public class AdminItemController {

    private final ItemRepository itemRepository;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String keyword) {
        List<Item> items;
        if (StringUtils.hasLength(keyword)) {
            items = itemRepository.findByNameContainingOrBrandContaining(keyword, keyword);
        } else {
            items = itemRepository.findAll();
        }
        return ResponseEntity.ok(items.stream().map(Item::toRead).toList());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> add(
            @RequestPart("data") ItemCreateRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        String normalizedCategory = ItemCategory.normalize(req.getCategory());
        if (!ItemCategory.isValid(normalizedCategory)) {
            return ResponseEntity.badRequest().body("Invalid category.");
        }

        String imgPath = (image != null && !image.isEmpty())
                ? fileStorageService.store(image)
                : "https://picsum.photos/seed/new/400/500";

        Item item = Item.builder()
                .brand(req.getBrand())
                .name(req.getName())
                .category(normalizedCategory)
                .description(req.getDescription())
                .price(req.getPrice())
                .discountPer(req.getDiscountPer() != null ? req.getDiscountPer() : 0)
                .stockCount(req.getStockCount() != null ? req.getStockCount() : 0)
                .viewCount(0)
                .imgPath(imgPath)
                .build();

        return ResponseEntity.ok(itemRepository.save(item).toRead());
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestPart("data") ItemCreateRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return itemRepository.findById(id).map(item -> {
            if (StringUtils.hasLength(req.getBrand())) item.setBrand(req.getBrand());
            if (StringUtils.hasLength(req.getName())) item.setName(req.getName());
            if (StringUtils.hasLength(req.getCategory())) {
                String normalizedCategory = ItemCategory.normalize(req.getCategory());
                if (!ItemCategory.isValid(normalizedCategory)) {
                    return ResponseEntity.badRequest().body("Invalid category.");
                }
                item.setCategory(normalizedCategory);
            }
            if (StringUtils.hasLength(req.getDescription())) item.setDescription(req.getDescription());
            if (req.getPrice() != null) item.setPrice(req.getPrice());
            if (req.getDiscountPer() != null) item.setDiscountPer(req.getDiscountPer());
            if (req.getStockCount() != null) item.setStockCount(req.getStockCount());

            if (image != null && !image.isEmpty()) {
                fileStorageService.delete(item.getImgPath());
                item.setImgPath(fileStorageService.store(image));
            }
            return ResponseEntity.ok(itemRepository.save(item).toRead());
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return itemRepository.findById(id).map(item -> {
            fileStorageService.delete(item.getImgPath());
            itemRepository.delete(item);
            return ResponseEntity.ok().<Object>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        Integer stock = body.get("stockCount");
        if (stock == null || stock < 0) {
            return ResponseEntity.badRequest().body("Invalid stock count.");
        }
        return itemRepository.findById(id).map(item -> {
            item.setStockCount(stock);
            return ResponseEntity.ok(itemRepository.save(item).toRead());
        }).orElse(ResponseEntity.notFound().build());
    }
}
