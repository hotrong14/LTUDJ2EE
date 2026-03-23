package com.example.demo.Controller;

import com.example.demo.Model.Category;
import com.example.demo.Model.Product;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.ProductService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer categoryId,
                       @RequestParam(required = false, defaultValue = "all") String sort,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       Model model) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> productPage;

        // Luôn set sort vào model để giữ trạng thái trên UI
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);

        if ("price_asc".equals(sort) || "price_desc".equals(sort)) {
            // Sắp xếp theo giá — trả về List, không phân trang
            List<Product> products = "price_asc".equals(sort)
                    ? productService.getAllSortedByPriceAsc()
                    : productService.getAllSortedByPriceDesc();
            model.addAttribute("products", products);

        } else if (keyword != null && !keyword.isEmpty()) {
            // Tìm kiếm theo keyword có phân trang
            productPage = productService.searchByKeyword(keyword, pageable);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("hasPrevious", productPage.hasPrevious());
            model.addAttribute("hasNext", productPage.hasNext());

        } else if (categoryId != null) {
            // Lọc theo danh mục có phân trang
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                productPage = productService.getByCategory(category, pageable);
                model.addAttribute("products", productPage.getContent());
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", productPage.getTotalPages());
                model.addAttribute("hasPrevious", productPage.hasPrevious());
                model.addAttribute("hasNext", productPage.hasNext());
            }

        } else {
            // Hiển thị tất cả sản phẩm với phân trang
            productPage = productService.getAllWithPagination(pageable);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("hasPrevious", productPage.hasPrevious());
            model.addAttribute("hasNext", productPage.hasNext());
        }

        model.addAttribute("categories", categoryService.getAll());
        return "product/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/add";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Product product,
                         BindingResult bindingResult,
                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                         @RequestParam(value = "imageFile", required = false) MultipartFile file,
                         RedirectAttributes redirectAttributes,
                         Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/add";
        }

        if (categoryId == null) {
            bindingResult.rejectValue("category", "error.category", "Vui lòng chọn danh mục");
            model.addAttribute("categories", categoryService.getAll());
            return "product/add";
        }

        Category category = categoryService.getById(categoryId);
        if (category == null) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/add";
        }

        product.setCategory(category);

        if (file != null && !file.isEmpty()) {
            String fileName = productService.uploadImage(file);
            product.setImage(fileName);
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Product product,
                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                         @RequestParam(value = "imageFile", required = false) MultipartFile file) throws Exception {

        Product existing = productService.getById(id);
        if (existing == null) {
            return "redirect:/products";
        }

        existing.setName(product.getName());
        existing.setPrice(product.getPrice());

        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            existing.setCategory(category);
        }

        if (file != null && !file.isEmpty()) {
            String fileName = productService.uploadImage(file);
            existing.setImage(fileName);
        }

        productService.save(existing);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}