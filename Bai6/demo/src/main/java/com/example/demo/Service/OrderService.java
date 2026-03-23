package com.example.demo.Service;

import com.example.demo.Model.Order;
import com.example.demo.Model.OrderDetail;
import com.example.demo.Model.Account;
import com.example.demo.Model.CartItem;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.OrderDetailRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, 
                        CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public Order createOrder(Account account, HttpSession session) {
        List<CartItem> cart = cartService.getCart(session);
        
        if (cart.isEmpty()) {
            return null;
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setAccount(account);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cartService.getCartTotal(session));

        Order savedOrder = orderRepository.save(order);

        // Tạo các chi tiết đơn hàng
        for (CartItem cartItem : cart) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProduct(productService.getById(cartItem.getProductId()));
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetailRepository.save(orderDetail);
        }

        // Xóa giỏ hàng sau khi tạo đơn hàng
        cartService.clearCart(session);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByAccount(Account account) {
        return orderRepository.findByAccount(account);
    }
}
