package com.example.demo.Service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Model.Category;

@Service
public class CategoryService {

    public List<Category> getAll() {
        return Arrays.asList(
            new Category(1, "Electronics"),
            new Category(2, "Books"),
            new Category(3, "Clothing")
        );
    }

    public Category get(int categoryId) {
        return (Category) getAll().stream()
            .filter(category -> category.getId() == categoryId)
            .findFirst()
            .orElse(null);
    }
    
}
