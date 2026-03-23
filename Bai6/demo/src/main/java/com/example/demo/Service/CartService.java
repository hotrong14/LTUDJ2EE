package com.example.demo.Service;

import com.example.demo.Model.CartItem;
import com.example.demo.Model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Product product, Integer quantity) {
        List<CartItem> cart = getCart(session);
        
        CartItem existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setImage(product.getImage());
            newItem.setQuantity(quantity);
            cart.add(newItem);
        }
        
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId().equals(productId));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateQuantity(HttpSession session, Long productId, Integer quantity) {
        List<CartItem> cart = getCart(session);
        CartItem item = cart.stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item != null) {
            if (quantity <= 0) {
                cart.remove(item);
            } else {
                item.setQuantity(quantity);
            }
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public Long getCartTotal(HttpSession session) {
        List<CartItem> cart = getCart(session);
        return cart.stream()
                .mapToLong(CartItem::getTotalPrice)
                .sum();
    }

    public Integer getCartItemCount(HttpSession session) {
        List<CartItem> cart = getCart(session);
        return cart.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
