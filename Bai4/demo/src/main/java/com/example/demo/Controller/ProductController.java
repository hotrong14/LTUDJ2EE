package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public String Index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    @GetMapping("/create")
    public String Create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    @PostMapping("/create")
    public String Create(@Valid Product newProduct, 
                         BindingResult result, 
                         @RequestParam("category.id") int categoryId, 
                         @RequestParam("imageProduct") MultipartFile imageProduct, 
                         Model model) {
        System.out.println("Received product: " + newProduct);
        System.out.println("Category ID: " + categoryId);
        System.out.println("Image file: " + imageProduct.getOriginalFilename());

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }
        
        try {
            productService.updateImage(newProduct, imageProduct); // Xử lý ảnh
            Category selectedCategory = categoryService.get(categoryId);
            newProduct.setCategory(selectedCategory);
            productService.add(newProduct);
        } catch (Exception e) {
            System.err.println("Error while creating product: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to create product.");
            return "product/create";
        }

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String Edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);
        if (find == null) {
            return "error/404"; // Trang lỗi tùy chỉnh
        }
        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    @PostMapping("/edit")
    public String Edit(@Valid Product editProduct, 
                       BindingResult result, 
                       @RequestParam("category.id") int categoryId, 
                       @RequestParam("imageProduct") MultipartFile imageProduct, 
                       Model model) {
        System.out.println("Received product for update: " + editProduct);
        System.out.println("Image file: " + (imageProduct != null ? imageProduct.getOriginalFilename() : "No image"));
        System.out.println("Category ID: " + categoryId);

        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("errorMessage", "Validation failed. Please correct the errors and try again.");
            return "product/edit";
        }

        try {
            Category selectedCategory = categoryService.get(categoryId);
            if (selectedCategory == null) {
                throw new IllegalArgumentException("Invalid category ID: " + categoryId);
            }
            editProduct.setCategory(selectedCategory);

            if (imageProduct != null && !imageProduct.isEmpty()) {
                productService.updateImage(editProduct, imageProduct);
            }
            productService.update(editProduct);
        } catch (IllegalArgumentException e) {
            System.err.println("Error while updating product: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        } catch (Exception e) {
            System.err.println("Unexpected error while updating product: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAll());
            return "product/edit";
        }

        System.out.println("Product updated successfully. Redirecting to products page.");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String Delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}