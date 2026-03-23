package com.example.demo.Repository;

import com.example.demo.Model.Order;
import com.example.demo.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccount(Account account);
}
