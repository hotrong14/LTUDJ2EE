package com.example.demo.Controller;

import com.example.demo.Model.Account;
import com.example.demo.Model.Order;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final AccountRepository accountRepository;

    public OrderController(OrderService orderService, CartService cartService, AccountRepository accountRepository) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.accountRepository = accountRepository;
    }

    // Trang checkout
    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalAmount", cartService.getCartTotal(session));
        return "checkout/index";
    }

    // Đặt hàng
    @PostMapping
    public String placeOrder(Authentication authentication,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        
        if (cartService.getCart(session).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        // Lấy thông tin người dùng từ authentication
        String username = authentication.getName();
        Account account = accountRepository.findByLoginName(username).orElse(null);

        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản người dùng!");
            return "redirect:/checkout";
        }

        Order order = orderService.createOrder(account, session);

        if (order != null) {
            redirectAttributes.addFlashAttribute("success", "Đơn hàng của bạn đã được tạo thành công!");
            return "redirect:/checkout/" + order.getId();
        } else {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng!");
            return "redirect:/checkout";
        }
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            model.addAttribute("order", order);
            return "order/detail";
        }
        return "redirect:/products";
    }
}
