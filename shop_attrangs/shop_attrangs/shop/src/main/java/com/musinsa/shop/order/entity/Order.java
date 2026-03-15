package com.musinsa.shop.order.entity;

import com.musinsa.shop.order.dto.OrderRead;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 10)
    private String payment;

    @Column(length = 16)
    private String cardNumber;

    @Column(nullable = false)
    private Long amount;

    /** PAID / CANCELLED */
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PAID";

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public OrderRead toRead() {
        return OrderRead.builder()
                .id(id)
                .name(name)
                .address(address)
                .payment(payment)
                .amount(amount)
                .status(status)
                .createdAt(createdAt)
                .build();
    }
}
