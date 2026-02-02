package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    List<Product> listProduct = new ArrayList<>();

    public List<Product> getAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(Product newProduct) {
        int maxId = listProduct.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0);
        newProduct.setId(maxId + 1);
        listProduct.add(newProduct);
    }

    public void update(Product editProduct) {
        Product find = get(editProduct.getId());
        if (find == null) {
            throw new IllegalArgumentException("Product not found for update: ID " + editProduct.getId());
        }
        find.setPrice(editProduct.getPrice());
        find.setName(editProduct.getName());
        find.setCategory(editProduct.getCategory()); // Ensure category is updated
        if (editProduct.getImage() != null) {
            find.setImage(editProduct.getImage());
        }
        System.out.println("Updated product: " + find);
    }

    public void updateImage(Product newProduct, MultipartFile imageProduct) {
        String contentType = imageProduct.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Uploaded file is not a valid image!");
        }

        if (!imageProduct.isEmpty()) {
            try {
                Path dirImages = Paths.get("static/images");
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }

                String newFileName = UUID.randomUUID() + "_" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);

                Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);

                newProduct.setImage(newFileName);
            } catch (IOException e) {
                throw new RuntimeException("Error saving image file: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Image file is empty. Please upload a valid image.");
        }
    }

    public void delete(int id) {
        Product find = get(id);
        if (find == null) {
            throw new IllegalArgumentException("Product not found for deletion: ID " + id);
        }
        listProduct.remove(find);
        System.out.println("Deleted product: " + find);
    }
}
