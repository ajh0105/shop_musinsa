package com.musinsa.shop.order.service;

import com.musinsa.shop.order.dto.OrderRead;
import com.musinsa.shop.order.dto.OrderRequest;

import java.util.List;

public interface OrderService {

    List<OrderRead> findAll(Integer memberId);

    OrderRead find(Integer id, Integer memberId);

    void order(OrderRequest orderReq, Integer memberId);

    boolean cancel(Integer orderId, Integer memberId);

    OrderRead changeStatus(Integer orderId, String newStatus);
}
