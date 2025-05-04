package com.example.backend.service;

import com.example.backend.dto.ProductSizeResponse;
import com.example.backend.models.ProductSize;
import com.example.backend.models.ProductSizeId;
import com.example.backend.repository.ProductSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSizeService {

    @Autowired
    private ProductSizeRepository repository;

    public List<ProductSize> getAll() {
        return repository.findAll();
    }

    public List<ProductSize> getByProductId(Integer productId) {
        return repository.findByProductId(productId);
    }

    public ProductSize save(ProductSize productSize) {
        return repository.save(productSize);
    }

    public void delete(Integer productId, Integer sizeId) {
        repository.deleteById(new ProductSizeId(productId, sizeId));
    }

    public ProductSize update(ProductSize productSize) {
        return repository.save(productSize);
    }

    public List<ProductSizeResponse> getAllDetails() {
        return repository.findAllProductSizeDetails();
    }
}
