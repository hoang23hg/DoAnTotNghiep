package com.example.backend.models;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeId implements Serializable {

    private Integer productId;
    private Integer sizeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSizeId)) return false;
        ProductSizeId that = (ProductSizeId) o;
        return Objects.equals(productId, that.productId) &&
               Objects.equals(sizeId, that.sizeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, sizeId);
    }
}
