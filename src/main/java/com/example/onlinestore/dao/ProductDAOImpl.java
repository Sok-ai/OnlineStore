package com.example.onlinestore.dao;

import com.example.onlinestore.entity.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

public class ProductDAOImpl implements ProductDAO {
    private final SessionFactory sessionFactory;

    public ProductDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addProduct(Product product) {
        if (product == null || product.getName() == null || product.getPrice() == null) {
            throw new IllegalArgumentException("Product, name, and price must not be null");
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в добавлении продукта");
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        }
    }


    @Override
    public Product getProductById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateProduct(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в обновлении продукта");
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        }
    }

    @Override
    public void removeProduct(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Ошибка в удалении продукта");
            e.printStackTrace();
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
                sessionFactory.getCurrentSession().getTransaction().rollback();
            }
        }
    }
}
