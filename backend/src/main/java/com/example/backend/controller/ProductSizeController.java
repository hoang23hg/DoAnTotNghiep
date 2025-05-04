package com.example.backend.controller;

import com.example.backend.models.ProductSize;
import com.example.backend.service.ProductSizeService;
import com.example.backend.dto.ProductSizeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-sizes")
public class ProductSizeController {

    @Autowired
    private ProductSizeService service;

    @GetMapping
    public ResponseEntity<List<ProductSize>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductSize>> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(service.getByProductId(productId));
    }

    @GetMapping("/details")
    public ResponseEntity<List<ProductSizeResponse>> getAllDetails() {
        return ResponseEntity.ok(service.getAllDetails());
    }

    @PostMapping
    public ResponseEntity<ProductSize> create(@RequestBody ProductSize productSize) {
        return ResponseEntity.ok(service.save(productSize));
    }

    @PutMapping
    public ResponseEntity<ProductSize> update(@RequestBody ProductSize productSize) {
        return ResponseEntity.ok(service.update(productSize));
    }

    @DeleteMapping("/{productId}/{sizeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer productId, @PathVariable Integer sizeId) {
        service.delete(productId, sizeId);
        return ResponseEntity.noContent().build();
    }
}
