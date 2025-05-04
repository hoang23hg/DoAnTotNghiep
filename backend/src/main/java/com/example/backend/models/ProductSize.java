package com.example.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_size")
@IdClass(ProductSizeId.class)
public class ProductSize {

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Id
    @Column(name = "size_id")
    private Integer sizeId;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id", insertable = false, updatable = false)
    private Size size;
}
