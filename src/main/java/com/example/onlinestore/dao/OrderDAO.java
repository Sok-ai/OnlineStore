package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderDAO {
    void addOrder(Order order);

    Order getOrderById(UUID id);

    List<Order> getOrdersByCustomerName(String customerName);

    void removeOrder(Order order);

    void updateOrder(Order order);
}
