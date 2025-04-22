package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.UUID;

public class OrderDAOImpl implements OrderDAO {
    private final SessionFactory sessionFactory;

    public OrderDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addOrder(Order order) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(order);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if (e.getSQLException() instanceof SQLIntegrityConstraintViolationException) {
                throw new IllegalArgumentException("Заказ должен содержать хотя бы один продукт", e);
            }
            throw new RuntimeException("Ошибка ограничения базы данных", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при добавлении заказа", e);
        }
    }

    @Override
    public Order getOrderById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Order.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске заказа по ID: " + id, e);
        }
    }

    @Override
    public List<Order> getOrdersByCustomerName(String customerName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select o FROM Order o WHERE customerName = :customerName", Order.class).setParameter("customerName", customerName).getResultList();
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
