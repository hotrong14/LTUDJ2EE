package com.example.demo.Controller;

import com.example.demo.Model.Product;
import com.example.demo.Service.CartService;
import com.example.demo.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    // Thêm vào giỏ hàng
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam(required = false, defaultValue = "1") Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Product product = productService.getById(productId);
        if (product != null) {
            cartService.addToCart(session, product, quantity);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được thêm vào giỏ hàng!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
        }
        return "redirect:/products";
    }

    // Hiển thị giỏ hàng
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalAmount", cartService.getCartTotal(session));
        model.addAttribute("itemCount", cartService.getCartItemCount(session));
        return "cart/view";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(session, productId);
        redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được xóa khỏi giỏ hàng!");
        return "redirect:/cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable Long productId,
                                 @RequestParam Integer quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(session, productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Số lượng đã được cập nhật!");
        return "redirect:/cart";
    }

    // Xóa tất cả giỏ hàng
    @GetMapping("/clear")
    public String clearCart(HttpSession session,
                            RedirectAttributes redirectAttributes) {
        cartService.clearCart(session);
        redirectAttributes.addFlashAttribute("success", "Giỏ hàng đã được xóa!");
        return "redirect:/cart";
    }
}
