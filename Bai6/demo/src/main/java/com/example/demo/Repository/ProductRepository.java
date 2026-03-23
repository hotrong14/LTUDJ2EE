package com.example.demo.Repository;
import com.example.demo.Model.Product;
import com.example.demo.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    List<Product> findByCategory(Category category);
    Page<Product> findByCategory(Category category, Pageable pageable);
}
