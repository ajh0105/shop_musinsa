package com.musinsa.shop.order.controller;

import com.musinsa.shop.common.util.SecurityUtil;
import com.musinsa.shop.order.dto.OrderRead;
import com.musinsa.shop.order.dto.OrderRequest;
import com.musinsa.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<?> readAll() {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        List<OrderRead> orders = orderService.findAll(memberId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Integer id) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        OrderRead order = orderService.find(id, memberId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody OrderRequest orderReq) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();
        try {
            orderService.order(orderReq, memberId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Integer id) {
        Integer memberId = securityUtil.getCurrentMemberId();
        if (memberId == null) return ResponseEntity.status(401).build();

        boolean cancelled = orderService.cancel(id, memberId);
        if (!cancelled) {
            return ResponseEntity.badRequest().body("취소할 수 없는 주문입니다.");
        }
        return ResponseEntity.ok().build();
    }
}
