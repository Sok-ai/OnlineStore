package com.example.onlinestore.util;

import com.example.onlinestore.entity.Order;
import com.example.onlinestore.entity.Product;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

import java.io.FileInputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);
            fis.close();

            // Создание конфигурации Hibernate
            Configuration configuration = new Configuration();

            // Добавление свойств в конфигурацию
            configuration.setProperty("hibernate.connection.driver_class", properties.getProperty("hibernate.connection.driver_class"));
            configuration.setProperty("hibernate.connection.url", properties.getProperty("hibernate.connection.url"));
            configuration.setProperty("hibernate.connection.username", properties.getProperty("hibernate.connection.username"));
            configuration.setProperty("hibernate.connection.password", properties.getProperty("hibernate.connection.password"));
            configuration.setProperty("hibernate.dialect", properties.getProperty("hibernate.dialect"));
            configuration.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("hibernate.hbm2ddl.auto"));
            configuration.setProperty("hibernate.show_sql", properties.getProperty("hibernate.show_sql"));
            configuration.setProperty("hibernate.format_sql", properties.getProperty("hibernate.format_sql"));

            // Добавление сущностей
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(Order.class);
            // добавляйте другие сущности здесь

            // Построение SessionFactory
            return configuration.buildSessionFactory();
        } catch (ServiceException e) {
            throw new RuntimeException("Ошибка подключения к базе данных. Проверьте:\n"
                    + "1. Запущен ли PostgreSQL\n"
                    + "2. Правильность логина/пароля\n"
                    + "3. Существование БД 'online_store'", e);
        } catch (Throwable ex) {
            throw new RuntimeException("Фатальная ошибка инициализации Hibernate", ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
