package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class OrderDAOImpl implements OrderDAO {
    private EntityManager em;

    @Override
    @Transactional
    public void addOrder(Order order) {
        em.persist(order);
    }

    @Override
    public Order getOrderById(UUID id) {
        return em.find(Order.class, id);
    }

    @Override
    public List<Order> getOrdersByCustomerName(String customerName) {
        return em.createQuery("SELECT o FROM Order o WHERE o.customerName = :customerName", Order.class)
                .setParameter("customerName", customerName)
                .getResultList();
    }

    @Override
    @Transactional
    public void removeOrder(Order order) {
        em.remove(order);
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        em.merge(order);
    }
}
