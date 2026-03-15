package com.musinsa.shop.order.service;

import com.musinsa.shop.cart.service.CartService;
import com.musinsa.shop.item.dto.ItemRead;
import com.musinsa.shop.item.entity.Item;
import com.musinsa.shop.item.repository.ItemRepository;
import com.musinsa.shop.item.service.ItemService;
import com.musinsa.shop.order.dto.OrderRead;
import com.musinsa.shop.order.dto.OrderRequest;
import com.musinsa.shop.order.entity.Order;
import com.musinsa.shop.order.entity.OrderItem;
import com.musinsa.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseOrderService implements OrderService {

    private static final String STATUS_CANCELLED = "CANCELLED";

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final CartService cartService;

    @Override
    public List<OrderRead> findAll(Integer memberId) {
        return orderRepository.findAllByMemberIdOrderByIdDesc(memberId).stream().map(Order::toRead).toList();
    }

    @Override
    public OrderRead find(Integer id, Integer memberId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndMemberId(id, memberId);
        if (orderOptional.isEmpty()) {
            return null;
        }

        OrderRead order = orderOptional.get().toRead();
        List<OrderItem> orderItems = orderItemService.findAll(order.getId());
        List<Integer> orderItemIds = orderItems.stream().map(OrderItem::getItemId).toList();
        List<ItemRead> items = itemService.findAll(orderItemIds);
        order.setItems(items);
        return order;
    }

    @Override
    @Transactional
    public void order(OrderRequest orderReq, Integer memberId) {
        if (orderReq.getItemIds() == null || orderReq.getItemIds().isEmpty()) {
            throw new IllegalArgumentException("주문할 상품이 없습니다.");
        }

        List<Integer> orderedItemIds = orderReq.getItemIds();
        Map<Integer, Long> quantityByItemId = orderedItemIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Item> items = itemRepository.findAllByIdIn(orderedItemIds);
        if (items.size() != quantityByItemId.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다.");
        }

        long amount = 0L;
        for (Item item : items) {
            int stock = item.getStockCount() == null ? 0 : item.getStockCount();
            int orderedQuantity = quantityByItemId.get(item.getId()).intValue();
            if (stock < orderedQuantity) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다: " + item.getName());
            }

            long salePrice = item.getPrice() - item.getPrice().longValue() * item.getDiscountPer() / 100;
            amount += salePrice * orderedQuantity;
            item.setStockCount(stock - orderedQuantity);
        }

        orderReq.setAmount(amount);
        Order order = orderRepository.save(orderReq.toEntity(memberId));

        List<OrderItem> newOrderItems = new ArrayList<>();
        orderedItemIds.forEach(itemId -> newOrderItems.add(new OrderItem(order.getId(), itemId)));
        orderItemService.saveAll(newOrderItems);
        itemRepository.saveAll(items);
        cartService.removeAll(order.getMemberId());
    }

    @Override
    @Transactional
    public boolean cancel(Integer orderId, Integer memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId).orElse(null);
        if (order == null || STATUS_CANCELLED.equals(order.getStatus())) {
            return false;
        }

        restoreStockByOrder(orderId);
        order.setStatus(STATUS_CANCELLED);
        orderRepository.save(order);
        return true;
    }

    @Override
    @Transactional
    public OrderRead changeStatus(Integer orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }

        String current = order.getStatus();
        if (!STATUS_CANCELLED.equals(current) && STATUS_CANCELLED.equals(newStatus)) {
            restoreStockByOrder(orderId);
        }
        if (STATUS_CANCELLED.equals(current) && !STATUS_CANCELLED.equals(newStatus)) {
            deductStockByOrder(orderId);
        }

        order.setStatus(newStatus);
        return orderRepository.save(order).toRead();
    }

    private void restoreStockByOrder(Integer orderId) {
        List<OrderItem> orderItems = orderItemService.findAll(orderId);
        updateStockByOrderItems(orderItems, 1);
    }

    private void deductStockByOrder(Integer orderId) {
        List<OrderItem> orderItems = orderItemService.findAll(orderId);
        updateStockByOrderItems(orderItems, -1);
    }

    private void updateStockByOrderItems(List<OrderItem> orderItems, int deltaUnit) {
        Map<Integer, Long> quantityByItemId = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getItemId, Collectors.counting()));
        List<Item> items = itemRepository.findAllByIdIn(new ArrayList<>(quantityByItemId.keySet()));

        for (Item item : items) {
            int stock = item.getStockCount() == null ? 0 : item.getStockCount();
            int quantity = quantityByItemId.get(item.getId()).intValue();
            int nextStock = stock + (deltaUnit * quantity);
            if (nextStock < 0) {
                throw new IllegalStateException("재고가 부족하여 주문 상태를 변경할 수 없습니다: " + item.getName());
            }
            item.setStockCount(nextStock);
        }

        itemRepository.saveAll(items);
    }
}
