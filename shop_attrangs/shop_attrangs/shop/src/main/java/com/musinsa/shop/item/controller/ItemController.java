package com.musinsa.shop.item.controller;

import com.musinsa.shop.item.dto.ItemRead;
import com.musinsa.shop.item.service.ItemService;
import com.musinsa.shop.item.support.ItemCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/api/items")
    public ResponseEntity<?> readAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword) {

        if (StringUtils.hasLength(keyword)) {
            return ResponseEntity.ok(itemService.search(keyword));
        }
        if ("new".equals(sort)) return ResponseEntity.ok(itemService.findNew());
        if ("best".equals(sort)) return ResponseEntity.ok(itemService.findBest());
        if ("recommend".equals(sort)) return ResponseEntity.ok(itemService.findRecommend());

        List<ItemRead> items;
        if (StringUtils.hasLength(category)) {
            String normalizedCategory = ItemCategory.normalize(category);
            if (!ItemCategory.isValid(normalizedCategory)) {
                return ResponseEntity.badRequest().body("지원하지 않는 카테고리입니다.");
            }
            items = itemService.findByCategory(List.of(normalizedCategory));
        } else {
            items = itemService.findAll();
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/api/items/{id}")
    public ResponseEntity<?> readOne(@PathVariable Integer id) {
        return itemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/items/{id}/related")
    public ResponseEntity<?> related(@PathVariable Integer id) {
        return itemService.findById(id).map(item ->
                ResponseEntity.ok(itemService.findRelated(item.getCategory(), id))
        ).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) return ResponseEntity.notFound().build();

            String contentType = "image/jpeg";
            if (filename.toLowerCase().endsWith(".png")) contentType = "image/png";
            else if (filename.toLowerCase().endsWith(".gif")) contentType = "image/gif";
            else if (filename.toLowerCase().endsWith(".webp")) contentType = "image/webp";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
