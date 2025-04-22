package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class OrderDAOImpl implements OrderDAO {
    private final SessionFactory sessionFactory;

    public OrderDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addOrder(Order order) {
        if (order == null || order.getCustomerName() == null || order.getProducts() == null) {
            throw new IllegalArgumentException("Заказ, Имя покупателя и продукты не могут быть null");
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в добавлении заказа");
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        }
    }

    @Override
    public Order getOrderById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Order.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> getOrdersByCustomerName(String customerName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select o FROM Order o WHERE customerName = :customerName", Order.class)
                    .setParameter("customerName", customerName)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeOrder(Order order) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в удалении заказа");
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        }
    }

    @Override
    public void updateOrder(Order order) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(order);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в обновлении заказа");
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        }
    }
}
