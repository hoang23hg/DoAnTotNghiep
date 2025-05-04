package com.example.backend.repository;

import com.example.backend.dto.ProductSizeResponse;
import com.example.backend.models.ProductSize;
import com.example.backend.models.ProductSizeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductSizeRepository extends JpaRepository<ProductSize, ProductSizeId> {

    List<ProductSize> findByProductId(Integer productId);

    @Query("SELECT new com.example.backend.dto.ProductSizeResponse(" +
       "p.productId, p.name, s.sizeId, CAST(s.sizeName AS string), ps.stockQuantity) " +
       "FROM ProductSize ps " +
       "JOIN Product p ON ps.productId = p.productId " +
       "JOIN Size s ON ps.sizeId = s.sizeId")
List<ProductSizeResponse> findAllProductSizeDetails();
}
