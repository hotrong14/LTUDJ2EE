package com.example.demo.Service;

import org.springframework.stereotype.Service;
import com.example.demo.Model.Product;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.CategoryRepository;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
