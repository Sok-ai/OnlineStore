package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Product;

import java.util.UUID;

public interface ProductDAO {
    Product getProductById(UUID id);

    void addProduct(Product product);

    void updateProduct(Product product);

    void removeProduct(Product product);
}
