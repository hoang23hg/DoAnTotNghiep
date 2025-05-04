package com.example.backend.service;

import com.example.backend.models.Category;
import com.example.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /** Lấy danh sách danh mục */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /** Tìm danh mục theo ID */
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    /** Tìm danh mục theo tên */
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword);
    }

    /** Thêm danh mục mới */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /** Cập nhật danh mục */
    public Optional<Category> updateCategory(Integer id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(categoryDetails.getCategoryName());
            category.setImageUrl(categoryDetails.getImageUrl());
            return categoryRepository.save(category);
        });
    }

    /** Xóa danh mục */
    public boolean deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
