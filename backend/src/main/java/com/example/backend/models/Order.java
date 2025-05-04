package com.example.backend.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false)
    private User user;  // Ánh xạ đúng quan hệ, không cần biến `uid` riêng

    @Column(name = "order_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "payment_method")
    private String paymentMethod;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetail> orderDetails;
}
