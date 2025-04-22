package com.example.onlinestore.service;

import com.example.onlinestore.dao.OrderDAO;
import com.example.onlinestore.dao.ProductDAO;
import com.example.onlinestore.entity.Order;
import com.example.onlinestore.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    public void createOrder(String customerName, List<UUID> products) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }

        Order order = new Order();
        order.setCustomerName(customerName);

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы один продукт");
        }

        for (UUID productId : products) {
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                throw new IllegalArgumentException("Продукт с ID " + productId + " не найден");
            }
            order.addProduct(product);
        }

        try {
            orderDAO.addOrder(order);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании заказа", e);
        }
    }

    public List<Order> getOrderByName(String customerName) {
        return orderDAO.getOrdersByCustomerName(customerName);
    }

    public void setJsonFile() throws IOException {
        List<Order> orders = orderDAO.getAllOrders();
        ObjectMapper objectMapper = new ObjectMapper();


        try (FileWriter fileWriter = new FileWriter("orders.json")) {

            if (orders == null || orders.isEmpty()) {
                objectMapper.writeValue(fileWriter, orders);
            }

            objectMapper.writeValue(fileWriter, orders);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка создания JSON файла", e);
        }
    }
}
