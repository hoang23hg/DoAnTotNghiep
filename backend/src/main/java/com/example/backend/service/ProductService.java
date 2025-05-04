package com.example.backend.service;

import com.example.backend.models.Product;
import com.example.backend.models.Category;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product addProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(product.getCategory().getCategoryId());
            category.ifPresent(product::setCategory);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(int id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStockQuantity(updatedProduct.getStockQuantity());
            product.setImageUrl(updatedProduct.getImageUrl());

            // Cập nhật category nếu có
            if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getCategoryId() != null) {
                Optional<Category> category = categoryRepository.findById(updatedProduct.getCategory().getCategoryId());
                category.ifPresent(product::setCategory);
            }

            return productRepository.save(product);
        }
        return null;
    }

    public String deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Sản phẩm đã được xóa!";
        }
        return "Không tìm thấy sản phẩm!";
    }
}
