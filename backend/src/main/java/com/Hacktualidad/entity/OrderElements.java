package com.Hacktualidad.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="orderElement")
public class OrderElements {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="orderElement_id", nullable = false)
    private Long orderElementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "product_id")
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double pricePerUnit;
}