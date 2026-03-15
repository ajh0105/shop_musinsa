package com.musinsa.shop.order.repository;

import com.musinsa.shop.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByMemberIdOrderByIdDesc(Integer memberId);

    Optional<Order> findByIdAndMemberId(Integer id, Integer memberId);

    List<Order> findAllByOrderByCreatedAtDesc();

    long countByStatus(String status);
}
