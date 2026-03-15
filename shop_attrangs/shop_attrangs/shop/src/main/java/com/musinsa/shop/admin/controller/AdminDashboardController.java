package com.musinsa.shop.admin.controller;

import com.musinsa.shop.item.repository.ItemRepository;
import com.musinsa.shop.member.repository.MemberRepository;
import com.musinsa.shop.order.repository.OrderRepository;
import com.musinsa.shop.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<?> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalMembers", memberRepository.count());
        data.put("totalItems", itemRepository.count());
        data.put("totalOrders", orderRepository.count());
        data.put("paidOrders", orderRepository.countByStatus("PAID"));
        data.put("cancelledOrders", orderRepository.countByStatus("CANCELLED"));
        data.put("totalReviews", reviewRepository.count());

        long totalSales = orderRepository.findAll().stream()
                .filter(o -> "PAID".equals(o.getStatus()))
                .mapToLong(o -> o.getAmount() != null ? o.getAmount() : 0L)
                .sum();
        data.put("totalSales", totalSales);

        data.put("recentOrders", orderRepository.findAllByOrderByCreatedAtDesc()
                .stream().limit(5).map(o -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", o.getId());
                    m.put("name", o.getName());
                    m.put("amount", o.getAmount());
                    m.put("status", o.getStatus());
                    m.put("createdAt", o.getCreatedAt());
                    return m;
                }).toList());

        return ResponseEntity.ok(data);
    }
}
