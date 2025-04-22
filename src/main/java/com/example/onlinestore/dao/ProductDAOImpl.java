package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class ProductDAOImpl implements ProductDAO {
    private EntityManager em;

    @Override
    public Product getProductById(UUID id) {
        return em.find(Product.class, id);
    }

    @Override
    @Transactional
    public void addProduct(Product product) {
        em.persist(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return em.createQuery("select p from Product p", Product.class).getResultList();
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        em.merge(product);
    }

    @Override
    @Transactional
    public void removeProduct(Product product) {
        em.remove(product);
    }
}
