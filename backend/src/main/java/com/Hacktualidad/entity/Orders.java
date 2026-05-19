package com.Hacktualidad.entity;

import com.Hacktualidad.Enums.Payment;
import com.Hacktualidad.Enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 600, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name ="payment", nullable = false)
    private Payment payment;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(name ="creation_date")
    private LocalDateTime creationDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "action")
    private Boolean action;
}