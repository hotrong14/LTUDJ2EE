package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private String productName;
    private Long price;
    private String image;
    private Integer quantity;

    public Long getTotalPrice() {
        return price * quantity;
    }
}
